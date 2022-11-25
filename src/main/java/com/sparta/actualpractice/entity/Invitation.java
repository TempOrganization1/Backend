package com.sparta.actualpractice.entity;


import com.sparta.actualpractice.dto.request.InvitationRequestDto;
import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_ID")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    public Invitation(String code, Member member, Party party) {

        this.code = code;
        this.member = member;
        this.party = party;
    }
}
