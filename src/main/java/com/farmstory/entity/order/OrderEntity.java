package com.farmstory.entity.order;

import com.farmstory.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`order`")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_idx")
    private Long orderIdx;

    @Column(name = "order_quantity")
    private int orderQuantity;

    @Column(name = "reciever_name", nullable = false, length = 20)
    private String recieverName;

    @Column(name = "reciever_hp", nullable = false, length = 20)
    private String recieverHp;

    @Column(name = "reciever_addr", nullable = false, length = 255)
    private String reciever_addr;

    @Column(name = "order_buying_type", nullable = false, length = 10)
    private String orderBuyingType;

    @Column(name = "order_save_point", nullable = false)
    private BigDecimal orderSavePoint;

    @Column(name = "order_etc")
    private String orderEtc;

    @Column(name = "order_total_price")
    private BigDecimal orderTotalPrice;

    @Column(name = "order_create_at")
    @CreationTimestamp
    private Date orderCreateAt;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private List<OrderItemEntity> orderItems;

}
