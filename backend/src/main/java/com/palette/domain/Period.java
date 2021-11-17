package com.palette.domain;

import com.palette.dto.PeriodDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

    // 여행 시작 일자
    private LocalDate startDate;

    // 여행 종료 일자
    private LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Period(PeriodDto periodDto){
        startDate = periodDto.getStartDate();
        endDate = periodDto.getEndDate();
    }
}
