package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.participant.Participant;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Participant> participantList;


    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateContent(String content) {

        this.content = content;
    }

    public void updateTime(String date, String MeetTime) {

        this.time = date + " " + MeetTime;
    }

    public void updatePlace(String address, String placeName) {

        this.place = address + "," + placeName;
    }
}
