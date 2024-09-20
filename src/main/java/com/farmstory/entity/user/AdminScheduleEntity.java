package com.farmstory.entity.user;

import com.farmstory.requestdto.user.DeleteScheduleReqDto;
import com.farmstory.responsedto.user.GetAdminScheduleRespDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_idx")
    private Long scheduleIdx;

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

    public GetAdminScheduleRespDto toGetAdminScheduleRespDto() {
        return GetAdminScheduleRespDto.builder()
                .bgcolor(bgcolor)
                .color(color)
                .text(text)
                .year(year)
                .month(month)
                .date(date)
                .build();
    }

    public Long forDelete(DeleteScheduleReqDto dto){
        if(dto.getDate()==(date)&&dto.getYear()==(year)&&dto.getMonth()==(month)){
            return scheduleIdx;
        }

        return (long)0;
    }

    public String forEvent(String year, String month, String date){
        String result;
        if(this.year == Integer.parseInt(year) && this.month == Integer.parseInt(month) && this.date == Integer.parseInt(date)){
            result = "true";
        } else {
            result = "false";
        }
        return result;
    }
}
