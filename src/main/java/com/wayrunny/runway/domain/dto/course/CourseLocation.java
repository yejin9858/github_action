package com.wayrunny.runway.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wayrunny.runway.domain.entity.Course;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CourseLocation {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="course_id")
    private Course course;

    private Double longitude;
    private Double latitude;

    public CourseLocation(Course course, Double longitude, Double latitude){
        this.course = course;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
