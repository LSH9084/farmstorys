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
@Table(name = "event_snapshot")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventSnapShotEntity {
    @Id
    @Column(name = "event_idx")
    private Long eventIdx;

    @Column(name = "event_name", nullable = false, length = 40)
    private String eventName;

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType;

    @Column(name = "event_delivery", nullable = false)
    private BigDecimal eventDelivery;

    @Column(name = "event_price", nullable = false)
    private BigDecimal eventPrice;

    @Column(name = "event_discount", nullable = false)
    private BigDecimal eventDiscount;

    @Column(name = "event_save_point", nullable = false)
    private BigDecimal eventSavePoint;

    @Column(name = "event_stock", nullable = false)
    private Integer eventStock;

    @Column(name = "event_create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp eventCreateAt;

    @Column(name = "event_etc", nullable = false)
    private String eventEtc;

    public ProductEntity forEvent(){
        return ProductEntity.builder()
                .prodSavePoint(eventSavePoint)
                .prodIdx(eventIdx)
                .prodDiscount(eventDiscount)
                .prodPrice(eventPrice)
                .prodStock(eventStock)
                .prodName(eventName)
                .prodType(eventType)
                .prodDelivery(eventDelivery)
                .prodEtc(eventEtc)
                .prodCreateAt(eventCreateAt)
                .build();
    }
}
