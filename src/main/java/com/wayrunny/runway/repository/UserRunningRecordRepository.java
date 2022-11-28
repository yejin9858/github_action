package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.dto.gathering.GatheringRecordDto;
import com.wayrunny.runway.domain.dto.gathering.RunningRecordDto;
import com.wayrunny.runway.domain.entity.UserRunningRecord;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRunningRecordRepository extends JpaRepository<UserRunningRecord, Long> {

    @Query("select count(urr) " +
            "from UserRunningRecord urr " +
            "where urr.userId = :userId")
    Integer countAllByUserId(@Param("userId")String userId);

    @Query("select count(urr) " +
            "from UserRunningRecord urr " +
            "where urr.userId = :userId " +
            "and urr.startTime > :thisMonth")
    Integer countAllThisMonthRunningByUserId(@Param("userId")String userId, @Param("thisMonth")LocalDateTime thisMonth);

    @Query("select new com.wayrunny.runway.domain.dto.gathering.RunningRecordDto(urr.startTime, urr.endTime, c) " +
            "from UserRunningRecord urr, Course c " +
            "where urr.userId = :userId " +
            "and c.id = urr.courseId " +
            "and urr.endTime > :weekAgo")
    List<RunningRecordDto> findWeekAgoRunningRecordDtoByUserId(@Param("userId")String userId, @Param("weekAgo")LocalDateTime weekAgo);

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringRecordDto(g) " +
            "from Gathering g, UserGatheringRelation ugr " +
            "where ugr.gatheringId = g.id " +
            "and ugr.userId = :userId " +
            "and g.dateTime > :todayMidNight " +
            "order by g.dateTime")
    List<GatheringRecordDto> findGatheringDtoByUserId(@Param("userId")String userId, @Param("todayMidNight")LocalDateTime todayMidNight);

    Boolean existsByUserIdAndGatheringId(String userId, Long gatheringId);
}
