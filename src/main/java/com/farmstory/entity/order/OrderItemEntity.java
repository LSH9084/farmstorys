package com.farmstory.entity.order;

import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.product.ProductFileEntity;
import com.farmstory.entity.product.ProductSnapShotEntity;
import com.farmstory.responsedto.order.GetOrdersRespDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_idx")
    private Long orderitemIdx;

    @ManyToOne
    @JoinColumn(name = "order_idx")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "prod_idx")
    private ProductEntity product;

    @Column(name = "snapshot_idx")
    private Long SnapShotIdx;

    @ManyToOne
    @JoinColumn(name = "snapShot_snapshot_idx", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductSnapShotEntity snapshot;

    @Column(name = "order_item_quantity")
    private int orderItemQuantity;

    public GetOrdersRespDto toGetOrdersRespDto(String userName) {
        if(product != null){
            String prodFileName = null;
            if(product.getProductFiles()==null){
                prodFileName = "empty.png";
            }

            ProductFileEntity file = product.getProductFiles()
                    .stream().filter(v->v.getProdFileType().equals("list"))
                    .findFirst().orElse(null);

            prodFileName = file != null ? file.getProdFileName() : "empty.png";
            BigDecimal deli = new BigDecimal(0);

            if(order.getOrderTotalPrice().intValue() > 30000){
                deli = new BigDecimal(0);
            } else {
                deli = product.getProdDelivery();
            }

            return GetOrdersRespDto.builder()
                    .orderIdx(order.getOrderIdx())
                    .prodName(product.getProdName())
                    .prodPrice(product.getProdPrice())
                    .orderCreateAt(order.getOrderCreateAt())
                    .prodQuantity(order.getOrderQuantity())
                    .userName(userName)
                    .totalPrice(order.getOrderTotalPrice())
                    .fileName(prodFileName)
                    .itemQuantity(orderItemQuantity)
                    .prodDelivery(deli)
                    .filePath("/file/")
                    .build();
        } else {
            BigDecimal deli = new BigDecimal(0);

            if(order.getOrderTotalPrice().intValue() > 30000){
                deli = new BigDecimal(0);
            } else {
                deli = product.getProdDelivery();
            }
            return GetOrdersRespDto.builder()
                    .orderIdx(order.getOrderIdx())
                    .prodName(snapshot.getSnapshotName())
                    .prodPrice(snapshot.getSnapshotPrice())
                    .orderCreateAt(order.getOrderCreateAt())
                    .prodQuantity(order.getOrderQuantity())
                    .userName(userName)
                    .totalPrice(order.getOrderTotalPrice())
                    .fileName(snapshot.getSnapshotFileName())
                    .filePath("/file/")
                    .itemQuantity(orderItemQuantity)
                    .prodDelivery(deli)
                    .build();
        }

    }
}
