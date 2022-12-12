package com.sparta.actualpractice.party;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.party.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Admin(Member member, Party party) {

        this.party = party;
        this.member = member;
    }

}
