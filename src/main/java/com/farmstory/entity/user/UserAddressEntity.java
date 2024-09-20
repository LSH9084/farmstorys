package com.farmstory.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_address")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addr_idx")
    private Long addrIdx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "addr_zone", nullable = false, length = 5)
    private String addrZone;

    @Column(name = "addr", nullable = false, length = 60)
    private String addr;

    @Column(name = "addr_detail", length = 60)
    private String addrDetail;
}
