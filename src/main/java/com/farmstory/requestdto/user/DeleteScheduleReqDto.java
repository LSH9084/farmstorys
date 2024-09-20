package com.farmstory.requestdto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeleteScheduleReqDto {
    private int year;
    private int month;
    private int date;
}
