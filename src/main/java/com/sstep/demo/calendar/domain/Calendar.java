package com.sstep.demo.calendar.domain;

import com.sstep.demo.staff.domain.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Calendar {
    @Id //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 생성을 DB에 위임 => DB에서 AUTO_INCREMENT 기능 사용
    private long id; //일정 고유번호
    private Date calendarDate; //일자
    private DayOfWeek dayOfWeek; //요일
    private LocalTime startCalTime;
    private LocalTime endCalTime;

    public Calendar(Date calendarDate, LocalTime startCalTime, LocalTime endCalTime) {
        this.calendarDate = calendarDate;
        this.startCalTime = startCalTime;
        this.endCalTime = endCalTime;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @ManyToOne
    private Staff staff;
}