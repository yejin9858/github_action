package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.dto.course.CourseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseLocationRepository extends JpaRepository<CourseLocation, Long> {
    List<CourseLocation> findByCourseId(String courseID);
}

