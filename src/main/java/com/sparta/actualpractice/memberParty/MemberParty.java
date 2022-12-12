package com.sparta.actualpractice.memberParty;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.party.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "MEMBER_PARTY")
public class MemberParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    public MemberParty(Member member, Party party) {
    
        this.member = member;
        this.party = party;
    }
}

