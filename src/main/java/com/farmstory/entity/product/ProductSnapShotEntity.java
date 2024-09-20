package com.farmstory.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "product_snapshot")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductSnapShotEntity {
    @Id
    @Column(name = "snapshot_idx")
    private Long snapshotIdx;

    @Column(name = "snapshot_name", nullable = false, length = 40)
    private String snapshotName;

    @Column(name = "snapshot_type", nullable = false, length = 20)
    private String snapshotType;

    @Column(name = "snapshot_delivery", nullable = false)
    private BigDecimal snapshotDelivery;

    @Column(name = "snapshot_price", nullable = false)
    private BigDecimal snapshotPrice;

    @Column(name = "snapshot_discount", nullable = false)
    private BigDecimal snapshotDiscount;

    @Column(name = "snapshot_save_point", nullable = false)
    private BigDecimal snapshotSavePoint;

    @Column(name = "snapshot_stock", nullable = false)
    private Integer snapshotStock;

    @Column(name = "snapshot_create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp snapshotCreateAt;

    @Column(name = "snapshot_etc", nullable = false)
    private String snapshotEtc;

    @Column(name = "snapshot_file_name")
    private String snapshotFileName;

    @Column(name = "snapshot_file_path")
    private String snapshotFilePath;


    public ProductEntity forEvent(){
        return ProductEntity.builder()
                .prodSavePoint(snapshotSavePoint)
                .prodIdx(snapshotIdx)
                .prodDiscount(snapshotDiscount)
                .prodPrice(snapshotPrice)
                .prodStock(snapshotStock)
                .prodName(snapshotName)
                .prodType(snapshotType)
                .prodDelivery(snapshotDelivery)
                .prodEtc(snapshotEtc)
                .prodCreateAt(snapshotCreateAt)
                .build();
    }

}
