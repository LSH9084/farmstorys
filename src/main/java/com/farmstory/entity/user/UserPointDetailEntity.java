package com.farmstory.entity.user;


import com.farmstory.responsedto.user.GetMypageUserPointRespDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_point_detail")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserPointDetailEntity {

    @Id
    @Column(name = "point_detail_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointDetailIdx;

    @Column(name = "save_point", nullable = true) // Default is nullable
    private BigDecimal savePoint;

    @Column(name = "use_point", nullable = true) // Default is nullable
    private BigDecimal usePoint;

    @Column(name = "detail", nullable = false )
    private String detail;

    @ManyToOne
    @JoinColumn(name = "point_idx")
    private UserPointEntity point;

    public GetMypageUserPointRespDto toGetMypageUserPointRestDto() {
        return GetMypageUserPointRespDto.builder()
                .detail(detail)
                .usePoint(usePoint)
                .savePoint(savePoint)
                .build();
    }
}
