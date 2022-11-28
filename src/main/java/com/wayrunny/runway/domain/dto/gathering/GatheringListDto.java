package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Gathering;
import com.wayrunny.runway.util.KeyService;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class GatheringListDto{
    private Long id;
    private String name;

    @JsonProperty("course_image")
    private String courseImage;

    @JsonIgnore
    private LocalDateTime localDateTime;

    @JsonProperty("date_time")
    private String dateTime;

    @JsonProperty("participated_person")
    private Integer participatedPerson;
    @JsonProperty("max_person")
    private Integer maxPerson;

    public GatheringListDto(Gathering gathering){
        this.id = gathering.getId();
        this.name = gathering.getName();
        this.courseImage = KeyService.addBaseURLToImage(gathering.getCourse().getImageUrl());
        this.localDateTime = gathering.getDateTime();
        this.participatedPerson = gathering.getParticipatedPerson();
        this.maxPerson = gathering.getMaxPerson();
        this.dateTime = this.localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
     }
}
