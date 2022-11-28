package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class TagCourseRelation {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="course_id")
    private String courseId;

    @NotNull
    @Column(name="tag_id")
    private String tagId;
}
