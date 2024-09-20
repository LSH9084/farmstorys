package com.farmstory.requestdto.user;

import com.farmstory.entity.user.UserScheduleEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostScheduleReqDto {
    private Long scheduleIdx;

    private Long userIdx;

    private int year;

    private int month;

    private int date;

    private String color;

    private String text;

    private String bgcolor;

    public UserScheduleEntity toEntity() {
        return UserScheduleEntity.builder()
                .userIdx(userIdx)
                .bgcolor(bgcolor)
                .color(color)
                .text(text)
                .year(year)
                .month(month)
                .date(date)
                .build();
    }
}
