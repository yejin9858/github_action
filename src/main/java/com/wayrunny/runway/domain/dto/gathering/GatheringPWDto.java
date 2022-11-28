package com.wayrunny.runway.domain.dto.gathering;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayrunny.runway.domain.entity.Gathering;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatheringPWDto {

    @NotNull
    @JsonProperty("gathering_id")
    private Long gatheringId;

    @NotNull
    @JsonProperty
    private String password;

    public GatheringPWDto(Gathering gathering){
        this.gatheringId = gathering.getId();
        this.password = gathering.getPassword();
    }

}