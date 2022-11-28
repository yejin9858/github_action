package com.wayrunny.runway.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Address {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String si;

    @NotNull
    private String gu;

    @NotNull
    private String dong;

    @OneToMany(mappedBy="address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<Course>();

    public Address(String si, String gu, String dong){
        this.si = si;
        this.gu = gu;
        this.dong = dong;
    }

    public void addNewCourse(Course course){
        this.courses.add(course);
    }

}
