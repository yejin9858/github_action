package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserRunningRecordDto {

    @NotNull
    @JsonProperty("start_date")
    private String startDate;

    @NotNull
    @JsonProperty("start_time")
    private String startTime;

    @NotNull
    @JsonProperty("end_date")
    private String endDate;

    @NotNull
    @JsonProperty("end_time")
    private String endTime;

    @JsonProperty("gathering_id")
    private Long gatheringId;

    @JsonProperty("course_id")
    private String courseId;
}
