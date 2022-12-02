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

    @Column(nullable = true)
    private String code;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<MemberParty> memberPartyList;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "party", cascade = CascadeType.REMOVE)
    private List<Album> albumList;

    @OneToOne(mappedBy = "party", cascade = CascadeType.REMOVE)
    private Admin admin;

    @OneToOne(mappedBy = "party", cascade = CascadeType.REMOVE)
    private ChatRoom chatRoom;


    public Party(String name, String introduction) {

        this.name = name;
        this.introduction = introduction;
    }


    public Party(PartyRequestDto partyRequestDto, String code) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
        this.code = code;
    }

    public void updateInformation(PartyRequestDto partyRequestDto) {

        this.name = partyRequestDto.getPartyName();
        this.introduction = partyRequestDto.getPartyIntroduction();
    }

    public void updateCode(String code) {

        this.code = code;
    }

    public void updateAdmin(Admin admin) {

        this.admin = admin;
    }

    public void updateChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
