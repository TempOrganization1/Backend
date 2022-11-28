package com.sparta.actualpractice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private Party party;

    private String chatRoomName;

    public ChatRoom(Party party, String chatRoomName) {
        this.party = party;
        this.chatRoomName = chatRoomName;
    }
}
