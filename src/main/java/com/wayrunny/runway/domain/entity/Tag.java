package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.wayrunny.runway.util.RandomGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @JsonIgnore
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.wayrunny.runway.util.RandomGenerator")
    private String id;

    @NotNull
    @Column(name="tag_name")
    @JsonProperty("tag_name")
    private String tagName;

    public Tag(String tagName){
        this.tagName = tagName;
    }
}
