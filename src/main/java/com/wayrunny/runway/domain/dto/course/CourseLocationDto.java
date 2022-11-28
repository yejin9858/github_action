package com.wayrunny.runway.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseLocationDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;


    public CourseLocationDto(CourseLocation courseLocation){
        this.latitude = courseLocation.getLatitude();
        this.longitude = courseLocation.getLongitude();
    }

    @Getter
    @Setter
    public static class CourseLocationAndId{

        @NotNull
        @JsonProperty("course_id")
        private String courseId;

        @NotNull
        @JsonProperty
        private Double longitude;

        @NotNull
        @JsonProperty
        private Double latitude;
    }
}
