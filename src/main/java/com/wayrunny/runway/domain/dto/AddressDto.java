package com.wayrunny.runway.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class AddressDto{

    @NotNull
    @JsonProperty
    private String si;

    @NotNull
    @JsonProperty
    private String gu;

    @NotNull
    @JsonProperty
    private String dong;

}