package com.wayrunny.runway.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.util.KeyService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CourseMainDto {

    @JsonProperty
    private List<CourseBoxSetWithGu> imageOnMains = new ArrayList<>();

    @JsonProperty
    private List<HashMap> coursesWithTopic = new ArrayList<>();

    public void addCoursesWithTopic(HashMap hashMapOfTopic){
        coursesWithTopic.add(hashMapOfTopic);
    }

    public static class CourseBoxSetWithGu {

        @JsonProperty
        private String id;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty
        private String gu;

        public CourseBoxSetWithGu(Course course) {
            this.id = course.getId();
            this.imageUrl = KeyService.addBaseURLToImage(course.getImageUrl());
            this.gu = course.getAddress().getGu();
        }
    }

    public static class CourseBoxSetWithNameLength {

        @JsonProperty
        private String id;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty
        private String name;

        @JsonProperty
        private Double length;

        public CourseBoxSetWithNameLength(Course course) {
            this.id = course.getId();
            this.imageUrl = KeyService.addBaseURLToImage(course.getImageUrl());
            this.name = course.getName();
            this.length = course.getLength();
        }
    }

}

