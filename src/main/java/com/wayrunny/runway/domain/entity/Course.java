package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.course.CourseDto;
import com.wayrunny.runway.domain.dto.course.CourseLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import com.wayrunny.runway.util.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor
public class Course extends Timestamped {

    @Id
    @JsonIgnore
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.wayrunny.runway.util.RandomGenerator")
    private String id;

    @NotEmpty
    private String name;

    private String content;

    private String imageUrl;

    private Double length;

    @NotNull
    @JsonIgnore
    private Integer scrapCount;

    @NotNull
    @JsonIgnore
    private Double starAverage;

    @NotNull
    @JsonIgnore
    private Integer starReviewCount;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="address_id")
    private Address address;

    private Double longitude;

    private Double latitude;

    @NotNull
    @OneToMany(mappedBy="course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourseLocation> locations = new ArrayList<CourseLocation>();

    public Course(CourseDto courseDto, Address address){
        this.name = courseDto.getName();
        this.imageUrl = courseDto.getImageUrl();
        this.content = courseDto.getContent();
        this.length = courseDto.getLength();
        this.scrapCount = 0;
        this.starAverage = 0.0;
        this.starReviewCount = 0;
        this.address = address;
        this.latitude = courseDto.getLatitude();
        this.longitude = courseDto.getLongitude();
    }

    public void addNewLocation(CourseLocation location){
        this.locations.add(location);
    }

}
