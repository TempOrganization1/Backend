package com.sparta.actualpractice.party;

import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.chat.ChatRoom;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.schedule.Schedule;
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


    public Party(String partyName, String partyInformation, String code) {

        this.name = partyName;
        this.introduction = partyInformation;
        this.code = code;
    }

    public void updateInformation(String partyName, String partyInformation) {

        this.name = partyName;
        this.introduction = partyInformation;
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
