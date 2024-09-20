package com.farmstory.service.order;

import com.farmstory.entity.cart.CartEntity;
import com.farmstory.entity.cart.CartItemEntity;
import com.farmstory.entity.order.OrderEntity;
import com.farmstory.entity.order.OrderItemEntity;
import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.product.ProductFileEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserPointDetailEntity;
import com.farmstory.entity.user.UserPointEntity;
import com.farmstory.repository.cart.CartItemRepository;
import com.farmstory.repository.cart.CartRepository;
import com.farmstory.repository.order.OrderItemRepository;
import com.farmstory.repository.order.OrderRepository;
import com.farmstory.repository.product.ProductRepository;
import com.farmstory.repository.user.UserPointDetailRepository;
import com.farmstory.repository.user.UserPointRepository;
import com.farmstory.repository.user.UserRepository;
import com.farmstory.requestdto.order.PostOrderDirectReqDto;
import com.farmstory.requestdto.order.PostOrderReqDto;
import com.farmstory.responsedto.order.GetOrderDirectRespDto;
import com.farmstory.responsedto.order.GetOrdersRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserPointRepository userPointRepository;
    private final UserPointDetailRepository userPointDetailRepository;
    private final ProductRepository productRepository;

    @Transactional
    public String insertOrder(PostOrderReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserEntity> optUser = userRepository.findByUserId(username);

        OrderEntity orderEntity =  request.toPostOrderEntity(optUser.get());

        orderRepository.save(orderEntity);

        long cartIdx = request.getCartIdx();
        CartEntity cart = cartRepository.findById(cartIdx).get();

        List<CartItemEntity> cartItems = cartItemRepository.findAllByCart(cart);

        cartItems.forEach(v->{
            OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                    .order(orderEntity)
                    .orderItemQuantity(v.getCartItemQuantity())
                    .product(v.getProd())
                    .SnapShotIdx(v.getProd().getProdIdx())
                    .build();
            orderItemRepository.save(orderItemEntity);

            ProductEntity productEntity = ProductEntity.builder()
                    .prodIdx(v.getProd().getProdIdx())
                    .prodName(v.getProd().getProdName())
                    .prodType(v.getProd().getProdType())
                    .prodDelivery(v.getProd().getProdDelivery())
                    .prodPrice(v.getProd().getProdPrice())
                    .prodDiscount(v.getProd().getProdDiscount())
                    .prodEtc(v.getProd().getProdEtc())
                    .prodSavePoint(v.getProd().getProdSavePoint())
                    .prodStock(v.getProd().getProdStock()-v.getCartItemQuantity())
                    .prodCreateAt(v.getProd().getProdCreateAt())
                    .build();

            productRepository.save(productEntity);
        });

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(optUser.get().getUserIdx());

        UserPointDetailEntity userPointDetailEntity2 = request.toUserPointDetailUseEntity("상품 구매 포인트사용",optUserPoint.get());
        userPointDetailRepository.save(userPointDetailEntity2);

        UserPointDetailEntity userPointDetailEntity1 = request.toUserPointDetailSaveEntity("상품 구매 포인트적립",optUserPoint.get());
        userPointDetailRepository.save(userPointDetailEntity1);

        List<UserPointDetailEntity> userPointDetails = userPointDetailRepository.findAllByPoint(optUserPoint.get());

        BigDecimal total = userPointDetails.stream()
                .map(detail -> detail.getSavePoint().subtract(detail.getUsePoint())) // savePoint에서 usePoint를 뺌
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 결과를 모두 더함

        UserPointEntity newUserPoint = UserPointEntity.builder()
                .userPoint(total)
                .userIdx(optUser.get().getUserIdx())
                .pointIdx(optUserPoint.get().getPointIdx())
                .build();

        userPointRepository.save(newUserPoint);


        cartItemRepository.deleteAll(cartItems);

        return "SU";
    }

    @Transactional
    public Page<GetOrdersRespDto> selectOrders(int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(page, 8);

        Page<OrderEntity> optOrder = orderRepository.findAllByUser(userEntity,pageable);



        List<OrderItemEntity> orderItemEntities = optOrder.stream()
                .flatMap(order->order.getOrderItems().stream()).toList();
        int total = orderItemEntities.size();

        List<GetOrdersRespDto> orderDtos = orderItemEntities.stream()
                .map(v->v.toGetOrdersRespDto(userEntity.getUserName()))
                .toList();


        List<GetOrdersRespDto> uniqueOrderDtos = orderDtos.stream()
                .collect(Collectors.toMap(
                        GetOrdersRespDto::getOrderIdx,  // Key extractor function
                        dto -> dto,                     // Value extractor function
                        (existing, replacement) -> existing // Merge function to keep existing entry
                ))
                .values()
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), uniqueOrderDtos.size());
        List<GetOrdersRespDto> pageContent = uniqueOrderDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);

    }

    @Transactional
    public List<GetOrdersRespDto> getOrder(long orderIdx) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        OrderEntity orderEntity = orderRepository.findById(orderIdx).get();

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findAllByOrder(orderEntity);
        orderItemEntities.stream().filter(v->{
            return v.getOrder().getUser().getUserIdx().equals(userEntity.getUserIdx());
        }).collect(Collectors.toList());

        List<GetOrdersRespDto> orderDtos = orderItemEntities.stream()
                .map(v->v.toGetOrdersRespDto(userEntity.getUserName())).collect(Collectors.toList());



        return orderDtos;
    }

    public GetOrderDirectRespDto selectOrderDirect(Long prodIdx, int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userEntity.getUserIdx());
        BigDecimal userPoint = optUserPoint.get().getUserPoint();

        Optional<ProductEntity> optProduct = productRepository.findById(prodIdx);

        ProductFileEntity prodFileEntity = optProduct.get().getProductFiles()
                .stream()
                .filter(v-> v.getProdFileType().equals("list"))
                .findFirst()
                .orElse(null);

        String fileName;
        if(prodFileEntity == null) {
            fileName = "empty.png";
        } else {
            fileName = prodFileEntity.getProdFileName();
        }

        BigDecimal delivery ;

        if (optProduct.get().getProdPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .compareTo(BigDecimal.valueOf(30000)) > 0) {
            delivery = BigDecimal.ZERO;
        } else {
            delivery = optProduct.get().getProdDelivery();
        }

        BigDecimal prodDiscount = optProduct.get().getProdPrice().multiply(BigDecimal.valueOf(quantity))
                .multiply(optProduct.get().getProdDiscount())
                .divide(new BigDecimal(100));

        BigDecimal discount = prodDiscount.negate();

        BigDecimal totalPrice = optProduct.get().getProdPrice().multiply(new BigDecimal(quantity)).add(discount).add(delivery);

        BigDecimal savePoint = optProduct.get().getProdSavePoint().multiply(new BigDecimal(quantity));

        GetOrderDirectRespDto getOrderDirectRespDto = GetOrderDirectRespDto.builder()
                .orderItemQuantity(quantity)
                .prodDelivery(delivery)
                .userPoint(userPoint)
                .prodPrice(optProduct.get().getProdPrice())
                .prodIdx(optProduct.get().getProdIdx())
                .prodName(optProduct.get().getProdName())
                .fileName(fileName)
                .filePath("/file/")
                .totalPrice(totalPrice)
                .orderSavePoint(savePoint)
                .build();

        return getOrderDirectRespDto;
    }

    @Transactional
    public void insertOrderDirect(PostOrderDirectReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        OrderEntity orderEntity = request.toOrderEntity(userEntity);

        orderRepository.save(orderEntity);

        Optional<ProductEntity> optProduct = productRepository.findById(request.getProdIdx());
        if(optProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        OrderItemEntity orderItemEntity = request.toOrderItemEntity(orderEntity,optProduct.get());

        orderItemRepository.save(orderItemEntity);

        ProductEntity productEntity = ProductEntity.builder()
                .prodIdx(optProduct.get().getProdIdx())
                .prodName(optProduct.get().getProdName())
                .prodType(optProduct.get().getProdType())
                .prodDelivery(optProduct.get().getProdDelivery())
                .prodPrice(optProduct.get().getProdPrice())
                .prodDiscount(optProduct.get().getProdDiscount())
                .prodEtc(optProduct.get().getProdEtc())
                .prodSavePoint(optProduct.get().getProdSavePoint())
                .prodStock(optProduct.get().getProdStock()- request.getOrderQuantity())
                .prodCreateAt(optProduct.get().getProdCreateAt())
                .build();

        productRepository.save(productEntity);

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userEntity.getUserIdx());

        UserPointDetailEntity userPointDetailEntity2 = request.toUserPointDetailUseEntity("상품 구매 포인트사용",optUserPoint.get());
        userPointDetailRepository.save(userPointDetailEntity2);

        UserPointDetailEntity userPointDetailEntity1 = request.toUserPointDetailSaveEntity("상품 구매 포인트적립",optUserPoint.get());
        userPointDetailRepository.save(userPointDetailEntity1);

        List<UserPointDetailEntity> userPointDetails = userPointDetailRepository.findAllByPoint(optUserPoint.get());

        BigDecimal total = userPointDetails.stream()
                .map(detail -> detail.getSavePoint().subtract(detail.getUsePoint())) // savePoint에서 usePoint를 뺌
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 결과를 모두 더함

        UserPointEntity newUserPoint = UserPointEntity.builder()
                .userPoint(total)
                .userIdx(userEntity.getUserIdx())
                .pointIdx(optUserPoint.get().getPointIdx())
                .build();

        userPointRepository.save(newUserPoint);

    }
}
