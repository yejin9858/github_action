package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.gathering.UserRunningRecordDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserRunningRecord {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull
    @JoinColumn(name ="course_id")
    private String courseId;

    @NotNull
    @Column(name="user_id")
    private String userId;

    @Column(name="gathering_id")
    private Long gatheringId;

    public UserRunningRecord(String userId, Long gatheringId, String courseId, LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseId = courseId;
        this.userId = userId;
        this.gatheringId = gatheringId;
    }
}
