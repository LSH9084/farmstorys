package com.farmstory.requestdto.order;

import com.farmstory.entity.order.OrderEntity;
import com.farmstory.entity.order.OrderItemEntity;
import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserPointDetailEntity;
import com.farmstory.entity.user.UserPointEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostOrderDirectReqDto {
    private BigDecimal orderTotalPrice;
    private BigDecimal orderSavePoint;
    private int orderQuantity;
    private String recieverName;
    private String recieverHp;
    private String recieverAddr;
    private String orderBuyingType;
    private String orderEtc;
    private Long prodIdx;
    private BigDecimal usePoint;

    public OrderEntity toOrderEntity(UserEntity user) {
        return OrderEntity.builder()
                .orderTotalPrice(orderTotalPrice)
                .orderSavePoint(orderSavePoint)
                .user(user)
                .orderQuantity(1)
                .orderEtc(orderEtc)
                .orderBuyingType(orderBuyingType)
                .reciever_addr(recieverAddr)
                .recieverHp(recieverHp)
                .recieverName(recieverName)
                .build();
    }

    public OrderItemEntity toOrderItemEntity(OrderEntity orderEntity, ProductEntity productEntity) {
        return OrderItemEntity.builder()
                .order(orderEntity)
                .product(productEntity)
                .SnapShotIdx(productEntity.getProdIdx())
                .orderItemQuantity(orderQuantity)
                .build();
    }

    public UserPointDetailEntity toUserPointDetailSaveEntity(String detail, UserPointEntity userPoint){
        return UserPointDetailEntity.builder()
                .detail(detail)
                .point(userPoint)
                .savePoint(orderSavePoint)
                .usePoint(BigDecimal.ZERO)
                .build();
    }

    public UserPointDetailEntity toUserPointDetailUseEntity(String detail, UserPointEntity userPoint){
        return UserPointDetailEntity.builder()
                .detail(detail)
                .point(userPoint)
                .savePoint(BigDecimal.ZERO)
                .usePoint(usePoint)
                .build();
    }
}
