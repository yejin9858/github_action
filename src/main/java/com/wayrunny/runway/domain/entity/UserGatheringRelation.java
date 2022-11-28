package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class UserGatheringRelation {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="user_id")
    private String userId;

    @NotNull
    @Column(name="gathering_id")
    private Long gatheringId;

    public UserGatheringRelation(String userId, Long gatheringId){
        this.userId = userId;
        this.gatheringId = gatheringId;
    }

}
