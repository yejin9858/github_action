package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.dto.course.CourseToViewDto;
import com.wayrunny.runway.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("select new com.wayrunny.runway.domain.dto.course.CourseToViewDto(c) from Course c")
    List<CourseToViewDto> findAllCourseToViewDto();

    @Query("select new com.wayrunny.runway.domain.dto.course.CourseToViewDto(c) " +
            "from Course c " +
            "where c.address.gu = :gu")
    List<CourseToViewDto> findAllCourseToViewDtoByAddressId(@Param("gu") String gu);

    @Query("select new com.wayrunny.runway.domain.dto.course.CourseToViewDto(c) " +
            "from Course c, TagCourseRelation tcr " +
            "where c.id = tcr.courseId " +
            "and tcr.tagId = :tagId")
    List<CourseToViewDto> findCourseToViewByTag(@Param("tagId") String tagId);

    @Query("select c " +
            "from Course c, TagCourseRelation tcr " +
            "where c.id = tcr.courseId " +
            "and tcr.tagId = :tagId")
    List<Course> findCourseByTag(@Param("tagId") String tagId);

    @Query("select c from Course c " +
            "where c.latitude - :latitude < 0.03 " +
            "and :latitude-c.latitude < 0.03 " +
            "and c.longitude - :longitude < 0.05 " +
            "and :longitude - c.longitude < 0.05 ")
    List<Course> findNearCourseFrom(@Param("latitude")Double latitude, @Param("longitude")Double longitude);

    @Query("select c " +
            "from Course c, Gathering g " +
            "where g.course.id = c.id " +
            "and g.id = :gatheringId")
    Optional<Course> findCourseByGatheringId(@Param("gatheringId") Long gatheringId);
}
