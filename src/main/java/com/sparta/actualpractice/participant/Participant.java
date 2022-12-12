package com.sparta.actualpractice.participant;


import com.sparta.actualpractice.schedule.Schedule;
import com.sparta.actualpractice.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Participant(Schedule schedule, Member member) {

        this.schedule = schedule;
        this.member = member;
    }
}

