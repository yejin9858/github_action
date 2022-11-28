package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.dto.gathering.GatheringListDto;
import com.wayrunny.runway.domain.dto.gathering.GatheringLookUpDto;
import com.wayrunny.runway.domain.entity.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringListDto(g) " +
            "from Course c, Gathering g " +
            "where c.id = g.course.id " +
            "and c.id = :courseId")
    List<GatheringListDto> findGatheringListDtoByCourseId(@Param("courseId")String courseId);

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringListDto(g)" +
            "from Gathering g " +
            "where g.participatedPerson < g.maxPerson " +
            "order by g.createdAt DESC ")
    List<GatheringListDto> findAllNewGathering();

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringListDto(g) " +
            "from Gathering g " +
            "where g.participatedPerson < g.maxPerson " +
            "order by g.dateTime, g.maxPerson-g.participatedPerson")
    List<GatheringListDto> findFastParticipatedGathering();

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringListDto(g) " +
            "from Gathering g " +
            "where g.name like CONCAT('%',:keyword,'%')")
    List<GatheringListDto> searchGatheringListDtoByCourseName(@Param("keyword")String keyword);

    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringLookUpDto(g) " +
            "from Gathering g, UserGatheringRelation ugr " +
            "where g.id = ugr.gatheringId " +
            "and ugr.userId = :userId " +
            "and g.dateTime > :todayMidNight " +
            "order by g.dateTime")
    List<GatheringLookUpDto> searchGatheringLookUpDtoByUser(@Param("userId")String userId, @Param("todayMidNight")LocalDateTime todayMidNight);


    @Query("select new com.wayrunny.runway.domain.dto.gathering.GatheringListDto(g) " +
            "from Gathering g " +
            "where g.dateTime > :bf1h " +
            "and g.dateTime < :af1h")
    List<GatheringListDto> findNearTimeGatheringListDto(@Param("bf1h") LocalDateTime nowBf1H,  @Param("af1h")LocalDateTime nowAf1H);
}