package com.sstep.demo.calendar.service;

import com.sstep.demo.calendar.CalendarRepository;
import com.sstep.demo.calendar.domain.Calendar;
import com.sstep.demo.calendar.dto.CalendarRequestDto;
import com.sstep.demo.calendar.dto.CalendarResponseDto;
import com.sstep.demo.schedule.domain.Schedule;
import com.sstep.demo.staff.StaffRepository;
import com.sstep.demo.staff.domain.Staff;
import com.sstep.demo.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final StaffRepository staffRepository;
    private final StaffService staffService;


    public void saveCalendar(CalendarRequestDto calendarRequestDto, Long staffId) {
        Staff staff = getStaffById(staffId);

        Calendar calendar = Calendar.builder()
                .calendarDate(calendarRequestDto.getCalendarDate())
                .dayOfWeek(calendarRequestDto.getDayOfWeek())
                .startCalTime(calendarRequestDto.getStartCalTime())
                .endCalTime(calendarRequestDto.getEndCalTime())
                .build();

        calendar.setStaff(staff);
        calendarRepository.save(calendar);

        Set<Calendar> calendars = getCalendarsByStaffId(staffId);
        calendars.add(calendar);
        staff.setCalendars(calendars);
        staffRepository.save(staff);
    }

    public Set<CalendarResponseDto> getDayWorkStaffs(Long storeId, String date, DayOfWeek day) {
        Set<CalendarResponseDto> staffs = new HashSet<>();
        for (Staff findStaff : calendarRepository.findDayWorkStaffsByDate(storeId, date, day)) {
            CalendarResponseDto dto = CalendarResponseDto.builder()
                    .staffName(findStaff.getMember().getName())
                    .startCalTime(getSchedule(findStaff, day).getStartTime())
                    .endCalTime(getSchedule(findStaff, day).getEndTime())
                    .dayOfWeek(day)
                    .calendarDate(date)
                    .build();

            staffs.add(dto);
        }
        return staffs;
    }

    private Schedule getSchedule(Staff findStaff, DayOfWeek dayOfWeek) {
        for (Schedule schedule : findStaff.getSchedules()) {
            if (schedule.getWeekDay() == dayOfWeek) {
                return schedule;
            }
        }
        throw new EntityNotFoundException();
    }

    private Staff getStaffById(Long staffId) {
        return staffService.getStaffById(staffId);
    }

    private Set<Calendar> getCalendarsByStaffId(Long staffId) {
        return calendarRepository.findCalendarsByStaffId(staffId);
    }
}
