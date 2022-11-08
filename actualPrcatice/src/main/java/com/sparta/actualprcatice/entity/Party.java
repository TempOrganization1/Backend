package com.sparta.actualprcatice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String introduction;

    @OneToMany(mappedBy = "party")
    private List<Member_Party> memberPartyList;

    @OneToMany(mappedBy = "party")
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "party")
    private List<Album> albumList;

    @OneToOne(mappedBy = "party")
    private Admin admin;




}
