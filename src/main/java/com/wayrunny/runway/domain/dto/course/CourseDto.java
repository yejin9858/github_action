package com.wayrunny.runway.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.AddressDto;
import com.wayrunny.runway.domain.entity.Address;
import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.domain.entity.Tag;
import com.wayrunny.runway.util.KeyService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    @NotNull
    private String name;

    @NotNull
    private String content;

    @JsonProperty("image_url")
    private String imageUrl;

    private Double length;

    private Double longitude;

    private Double latitude;

    public static class CourseToDetail{
        @NotNull
        @JsonProperty
        private String name;

        @JsonProperty("image_url")
        private String imageUrl;

        @NotNull
        @JsonProperty
        private String content;

        @NotNull
        @JsonProperty
        private Double length;

        @NotNull @JsonProperty
        private AddressDto address;

        @NotNull
        @JsonProperty("scrap_count")
        private Integer scrapCount;

        @NotNull
        @JsonProperty("star_average")
        private Double starAverage;

        @NotNull
        @JsonProperty("star_review_count")
        private Integer starReviewCount;

        @JsonProperty
        private List<Tag> tags;

        @JsonProperty
        private List<CourseLocationDto> locations;

        @NotNull
        @JsonProperty
        private Double longitude;

        @NotNull
        @JsonProperty
        private Double latitude;


        public CourseToDetail(Course course, List<Tag> tags){
            this.name = course.getName();
            this.imageUrl = KeyService.addBaseURLToImage(course.getImageUrl());
            this.content = course.getContent();
            this.length = course.getLength();
            this.scrapCount = course.getScrapCount();
            this.starAverage = course.getStarAverage();
            this.starReviewCount = course.getStarReviewCount();
            this.locations = new ArrayList<CourseLocationDto>();
            for(int i = 0 ; i < course.getLocations().size() ; i++) {
                this.locations.add(new CourseLocationDto(course.getLocations().get(i)));
            }
            Address tempAddress = course.getAddress();
            this.address = new AddressDto(tempAddress.getSi(), tempAddress.getGu(), tempAddress.getDong());
            this.tags = tags;
            this.latitude = course.getLatitude();
            this.longitude = course.getLongitude();
        }
    }

}