package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Gathering;
import com.wayrunny.runway.util.KeyService;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
public class GatheringLookUpDto {
    private Long id;
    private String name;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_image")
    private String courseImage;

    @JsonIgnore
    private LocalDateTime dateTime;

    private String date;
    private String time;

    public GatheringLookUpDto(Gathering gathering){
        this.id = gathering.getId();
        this.name = gathering.getName();
        this.courseImage = KeyService.addBaseURLToImage(gathering.getCourse().getImageUrl());
        this.courseName = gathering.getCourse().getName();
        this.dateTime = gathering.getDateTime();
        this.date = this.dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                + " " + this.dateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        this.time = this.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
     }
}
