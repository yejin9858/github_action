package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Course;
import com.wayrunny.runway.domain.entity.Gathering;
import com.wayrunny.runway.domain.entity.User;
import com.wayrunny.runway.util.KeyService;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Setter
public class GatheringDetailDto {
    @JsonProperty("gathering_name")
    private String gatheringName;

    @JsonProperty("gathering_id")
    private Long gatheringId;

    @JsonIgnore
    private LocalDateTime gatheringDateTime;

    @JsonProperty("gathering_date")
    private String gatheringDate;

    @JsonProperty("gathering_time")
    private String gatheringTime;

    @JsonProperty("master_user_name")
    private String masterUserName;

    @JsonProperty("master_user_id")
    private String masterUserId;


    @JsonProperty("master_user_image_url")
    private String masterUserImageUrl;

    @JsonProperty("master_user_level")
    private Integer masterUserLevel;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_image_url")
    private String courseImageUrl;

    @JsonProperty("course_content")
    private String courseContent;

    @JsonProperty("tag_list")
    private List<String> tagNameList = new ArrayList<>();

    @JsonProperty("participate_person")
    private Integer participatePerson;

    @JsonProperty("max_person")
    private Integer maxPerson;

    public GatheringDetailDto(Gathering gathering, List<String> tagNameList){
        User user= gathering.getUser();
        Course course = gathering.getCourse();
        this.gatheringName = gathering.getName();
        this.gatheringId = gathering.getId();
        this.gatheringDateTime = gathering.getDateTime();
        this.masterUserName = user.getNickname();
        this.masterUserId = user.getId();
        this.masterUserLevel = user.getLevel();
        this.courseName = course.getName();
        this.courseImageUrl = KeyService.addBaseURLToImage(course.getImageUrl());
        this.courseContent = course.getContent();
        this.participatePerson = gathering.getParticipatedPerson();
        this.maxPerson = gathering.getMaxPerson();
        this.tagNameList = tagNameList;
        if(user.getImageUrl() != null)
            this.masterUserImageUrl = KeyService.addBaseURLToImage(user.getImageUrl());
        this.gatheringDate = this.gatheringDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.gatheringTime = this.gatheringDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
