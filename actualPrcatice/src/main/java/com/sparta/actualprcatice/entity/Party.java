package com.sparta.actualprcatice.entity;

import com.sparta.actualprcatice.dto.request.PartyRequestDto;
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
    private List<MemberParty> memberPartyList;

    @OneToMany(mappedBy = "party")
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "party")
    private List<Album> albumList;

    @OneToOne(mappedBy = "party")
    private Admin admin;


    public Party(PartyRequestDto partyRequestDto) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
    }

    public void update(PartyRequestDto partyRequestDto) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
    }
}
