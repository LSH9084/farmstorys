package com.farmstory.service.cart;

import com.farmstory.entity.cart.CartEntity;
import com.farmstory.entity.cart.CartItemEntity;
import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserPointEntity;
import com.farmstory.repository.cart.CartItemRepository;
import com.farmstory.repository.cart.CartRepository;
import com.farmstory.repository.product.ProductRepository;
import com.farmstory.repository.user.UserPointRepository;
import com.farmstory.repository.user.UserRepository;
import com.farmstory.requestdto.cart.DeleteCartReqDto;
import com.farmstory.requestdto.cart.PostCartItemReqDto;
import com.farmstory.responsedto.cart.GetCartsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserPointRepository userPointRepository;


    public String insertCart(PostCartItemReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 사용자 조회
        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자 카트 조회
        Optional<CartEntity> optCart = cartRepository.findByUserIdx(userEntity.getUserIdx());
        CartEntity cartEntity;

        if (!optCart.isPresent()) {
            // 카트가 없으면 새로 생성
            cartEntity = CartEntity.builder()
                    .userIdx(userEntity.getUserIdx())
                    .build();
            cartEntity = cartRepository.save(cartEntity);
        } else {
            // 카트가 이미 있으면 그대로 사용
            cartEntity = optCart.get();
        }

        // 상품 조회
        ProductEntity productEntity = productRepository.findById((long) request.getProdIdx())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 카트 아이템이 이미 있는지 확인
        Optional<CartItemEntity> existingCartItem = cartItemRepository.findByCartAndProd(cartEntity, productEntity);

        if (existingCartItem.isPresent()) {
            // 이미 있는 아이템의 경우 수량 증가
            CartItemEntity cartItem = CartItemEntity.builder()
                    .cart(cartEntity)
                    .cartItemIdx(existingCartItem.get().getCartItemIdx())
                    .cartItemQuantity(existingCartItem.get().getCartItemQuantity()+request.getCartItemQuantity())
                    .prod(productEntity)
                    .build();
            cartItemRepository.save(cartItem);
        } else {
            // 새로운 카트 아이템 추가
            CartItemEntity cartItem = CartItemEntity.builder()
                    .cartItemQuantity(request.getCartItemQuantity())
                    .cart(cartEntity)
                    .prod(productEntity)
                    .build();
            cartItemRepository.save(cartItem);
        }

        return "SU";
    }


    @Transactional
    public List<GetCartsRespDto> selectCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        // 유저 정보 조회
        Optional<UserEntity> optUser = userRepository.findByUserId(userId);

        if (optUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        UserEntity user = optUser.get();

        // 카트 및 유저 포인트 조회
        Optional<CartEntity> optCart = cartRepository.findByUserIdx(user.getUserIdx());
        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(user.getUserIdx());


        // 카트가 없으면 생성
        CartEntity cart;
        if (optCart.isEmpty()) {
            cart = cartRepository.save(CartEntity.builder().userIdx(user.getUserIdx()).build());
        } else {
            cart = optCart.get();
        }

        // 카트 아이템 조회
        List<CartItemEntity> cartItems = cartItemRepository.findAllByCart(cart);

        // 카트 아이템을 DTO로 변환
        List<GetCartsRespDto> carts = cartItems.stream()
                .map(v -> v.toDtoGetCarts(user.getUserName(), user.getUserHp(), optUserPoint.get().getUserPoint()))
                .collect(Collectors.toList());

        // 결과 반환
        return carts;
    }

    @Transactional
    public String deleteCart(DeleteCartReqDto request) {
        request.getCartItemIdx().forEach(v->{
            cartItemRepository.delete(CartItemEntity.builder().cartItemIdx(Long.parseLong(v)).build());
        });

        return "SU";
    }
}
