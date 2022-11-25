package com.sparta.actualpractice.entity;

import com.sparta.actualpractice.dto.request.PartyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String introduction;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<MemberParty> memberPartyList;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<Album> albumList;

    @OneToOne(mappedBy = "party", cascade = CascadeType.ALL)
    private Admin admin;

    public Party(String name, String introduction) {

        this.name = name;
        this.introduction = introduction;
    }

    public Party(PartyRequestDto partyRequestDto) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
    }

    public void update(PartyRequestDto partyRequestDto) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
    }
}
