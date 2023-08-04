package com.sstep.demo.commute.domain;

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
public class Commute { //일자별 실 출근/퇴근 시간 도메인
    @Id //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //실 출퇴근시간 고유번호
    private Date commuteDate; //출퇴근 일자
    private DayOfWeek dayOfWeek; //출퇴근 요일
    private LocalTime startTime; //출근 시간
    private LocalTime endTime; //퇴근 시간
    private boolean isLate; //지각 여부
    private String disputeMessage; //출퇴근 관련 이의 신청 메시지

    public String getDisputeMessage() {
        return disputeMessage;
    }

    public void setDisputeMessage(String lateMessage) {
        this.disputeMessage = lateMessage;
    }

    public void setCommuteDate(Date commuteDate) {
        this.commuteDate = commuteDate;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    //직원 테이블과 1대다 조인
    @ManyToOne
    private Staff staff;

    //    // 1. LocalDate 생성
//    LocalDate date = LocalDate.of(2021, 12, 25);
//    // LocalDateTime date = LocalDateTime.of(2021, 12, 25, 1, 15, 20);
//        System.out.println(date);  // // 2021-12-25
//
//    // 2. DayOfWeek 객체 구하기
//    DayOfWeek dayOfWeek = date.getDayOfWeek();
//
//    // 3. 숫자 요일 구하기
//    int dayOfWeekNumber = dayOfWeek.getValue();
//
//    // 4. 숫자 요일 출력
//        System.out.println(dayOfWeekNumber);  //


}