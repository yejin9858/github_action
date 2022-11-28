package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.dto.gathering.GatheringDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Gathering extends Timestamped {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private String content;

    //참가한 인원 수  --> 어떻게..? --> 참여하기를 눌렀을때 해당 번개방에 해당 값에 +1하기
    private Integer participatedPerson = 1;

    @NotNull
    private Integer maxPerson;

    @NotNull
    private Boolean secret;

    private String password;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Gathering(GatheringDto gatheringDto, Course course, User user) {
        this.name = gatheringDto.getName();
        this.course = course;
        this.dateTime = gatheringDto.getDateTime();
        this.content = gatheringDto.getContent();
        this.maxPerson = gatheringDto.getMaxPerson();
        this.secret = gatheringDto.getSecret();
        this.password = gatheringDto.getPassword();
        this.user = user;
    }

    public void addParticipatedPerson(Integer manNum) {
        this.participatedPerson += manNum;
    }
}
