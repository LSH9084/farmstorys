package com.farmstory.requestdto.order;

import com.farmstory.entity.order.OrderEntity;
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
public class PostOrderReqDto {
    private int orderQuantity;
    private BigDecimal orderTotalPrice;
    private String orderBuyingType;
    private BigDecimal usePoint;
    private String recieverName;
    private String recieverHp;
    private String recieverAddr;
    private BigDecimal orderSavePoint;
    private String orderEtc;
    private long cartIdx;
    private int itemQuantity;

    public OrderEntity toPostOrderEntity(UserEntity user){
        return OrderEntity.builder()
                .user(user)
                .orderQuantity(orderQuantity)
                .orderSavePoint(orderSavePoint)
                .orderEtc(orderEtc)
                .orderBuyingType(orderBuyingType)
                .recieverName(recieverName)
                .recieverHp(recieverHp)
                .reciever_addr(recieverAddr)
                .orderTotalPrice(orderTotalPrice)
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
