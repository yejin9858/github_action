package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.entity.UserGatheringRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserGatheringRelationRepository extends JpaRepository<UserGatheringRelation, Long> {
    boolean existsByAndUserIdAndGatheringId(String userId, Long gatheringId);

    @Query("select count(r) " +
            "from UserGatheringRelation r, Gathering g " +
            "where r.userId = :userId " +
            "and g.id = r.gatheringId " +
            "and g.dateTime >= :now")
    Integer countAllPlannedRunningByUserId(@Param("userId")String userId, @Param("now") LocalDateTime now);

}
