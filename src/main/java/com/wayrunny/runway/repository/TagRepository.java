package com.wayrunny.runway.repository;

import com.wayrunny.runway.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    public Optional<Tag> findByTagName(String tagName);

    @Query("select t " +
            "from Tag t, TagCourseRelation tcr " +
            "where tcr.courseId = :courseId " +
            "and t.id = tcr.tagId")
    public List<Tag> findAllByCourseId(@Param("courseId") String courseId);

    @Query("select t.tagName " +
            "from Tag t, TagCourseRelation tcr " +
            "where tcr.courseId = :courseId " +
            "and t.id = tcr.tagId")
    public List<String> findAllTagNameByCourseId(@Param("courseId") String courseId);
}
