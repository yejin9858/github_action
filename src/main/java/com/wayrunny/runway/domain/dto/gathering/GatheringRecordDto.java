package com.wayrunny.runway.domain.dto.gathering;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Gathering;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class GatheringRecordDto {

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("gathering_id")
    private Long gatheringId;

    @JsonIgnore
    private LocalDateTime dateTime;

    private String date;

    public GatheringRecordDto(Gathering gathering){
        this.imageUrl = gathering.getCourse().getImageUrl();
        this.gatheringId = gathering.getId();
        this.dateTime =gathering.getDateTime();
        this.date = dateTime.format(DateTimeFormatter.ofPattern("MM/dd"));
    }
}
