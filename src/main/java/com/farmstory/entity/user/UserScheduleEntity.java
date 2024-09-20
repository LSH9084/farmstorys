package com.farmstory.entity.user;

import com.farmstory.requestdto.user.DeleteScheduleReqDto;
import com.farmstory.requestdto.user.PostScheduleReqDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_idx")
    private Long scheduleIdx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "`year`",nullable = false)
    private int year;

    @Column(name = "`month`",nullable = false)
    private int month;

    @Column(name = "`date`",unique = false,nullable = false)
    private int date;


    @Column(name = "`color`",length = 255)
    private String color;

    @Column(name = "`text`",length = 20)
    private String text;

    @Column(name = "`bgcolor`",length = 255)
    private String bgcolor;

    public PostScheduleReqDto toPostScheduleReqDto() {
        return PostScheduleReqDto.builder()
                .scheduleIdx(scheduleIdx)
                .year(year)
                .month(month)
                .date(date)
                .color(color)
                .scheduleIdx(scheduleIdx)
                .text(text)
                .bgcolor(bgcolor)
                .userIdx(userIdx)
                .build();
    }

    public Long forDelete(DeleteScheduleReqDto dto){
        if(dto.getDate()==(date)&&dto.getYear()==(year)&&dto.getMonth()==(month)){
            return scheduleIdx;
        }

        return (long)0;
    }
}
