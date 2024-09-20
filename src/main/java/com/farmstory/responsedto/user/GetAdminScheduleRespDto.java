package com.farmstory.responsedto.user;

import com.farmstory.entity.user.AdminScheduleEntity;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetAdminScheduleRespDto {
    private Long scheduleIdx;

    private int year;

    private int month;

    private int date;

    private String color;

    private String text;

    private String bgcolor;

    public AdminScheduleEntity toAdminScheduleEntity() {
        return AdminScheduleEntity.builder()
                .bgcolor(bgcolor)
                .color(color)
                .text(text)
                .year(year)
                .month(month)
                .date(date)
                .build();
    }
}
