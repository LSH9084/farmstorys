package com.farmstory.requestdto.user;

import com.farmstory.entity.user.AdminScheduleEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostAdminScheduleReqDto {
    private int year;

    private int month;

    private int date;

    private String color;

    private String text;

    private String bgcolor;

    public AdminScheduleEntity toAdminScheduleEntity() {
        return AdminScheduleEntity.builder()
                .year(year)
                .month(month)
                .date(date)
                .color(color)
                .text(text)
                .bgcolor(bgcolor)
                .build();
    }
}
