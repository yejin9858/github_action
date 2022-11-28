package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.wayrunny.runway.domain.entity.Gathering;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GatheringDto {

    @NotNull
    private String name;

    @NotNull
    @JsonProperty("date_time")
    private LocalDateTime dateTime;

    @NotNull
    private String content;

    @NotNull
    @JsonProperty("max_person")
    private Integer maxPerson;

    @NotNull
    private Boolean secret;

    private String password;


    @Getter
    @Setter
    public static class GatheringCreateDto{
        @NotNull
        @JsonProperty
        private String name;

        @NotNull
        @JsonProperty("course_id")
        private String courseId;

        @NotNull
        @JsonProperty
        private String time;

        @NotNull
        @JsonProperty
        private String date;

        @NotNull
        @JsonProperty
        private String content;

        @NotNull
        @JsonProperty("max_person")
        private Integer maxPerson;

        @NotNull
        @JsonProperty("participated_person")
        private Integer participatedPerson = 1;

        @NotNull
        @JsonProperty
        private Boolean secret;


        @JsonProperty
        private String password;
    }

    @Getter
    public static class GatheringJoinDto{

        @JsonProperty("gathering_id")
        private Long gatheringId;
    }

}
