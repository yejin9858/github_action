package com.wayrunny.runway.domain.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.AddressDto;
import com.wayrunny.runway.domain.entity.Address;
import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.util.KeyService;

public class CourseToViewDto {

    @NotNull
    @JsonProperty
    private String id;

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

    @NotNull
    @JsonProperty
    private Double length;

    @NotNull
    @JsonProperty
    private Double longitude;

    @NotNull
    @JsonProperty
    private Double latitude;

    public CourseToViewDto(Course course){
        this.id = course.getId();
        this.name = course.getName();
        this.imageUrl = KeyService.addBaseURLToImage(course.getImageUrl());
        this.content = course.getContent();
        this.length = course.getLength();
        this.scrapCount = course.getScrapCount();
        this.starAverage = course.getStarAverage();
        this.starReviewCount = course.getStarReviewCount();
        Address tempAddress = course.getAddress();
        this.address = new AddressDto(tempAddress.getSi(), tempAddress.getGu(), tempAddress.getDong());
        this.latitude = course.getLatitude();
        this.longitude = course.getLongitude();
    }
}
