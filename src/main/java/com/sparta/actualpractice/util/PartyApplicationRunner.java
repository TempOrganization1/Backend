package com.sparta.actualpractice.util;

import com.sparta.actualpractice.chat.ChatRoom;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.chat.ChatRoomRepository;
import com.sparta.actualpractice.member.MemberRepository;
import com.sparta.actualpractice.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyApplicationRunner implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatRoomRepository chatRoomRepository;
    private final PartyRepository partyRepository;
    private final OauthUtil oauthUtil;

    @Value("${admin.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!memberRepository.existsById(1L)) {

            Member member = Member.builder()
                    .email("admin@wef.com")
                    .name("관리자")
                    .password(passwordEncoder.encode(password))
                    .build();

            memberRepository.save(member);

            String name = "위프";
            String introduction = "모두와 함께 다양한 기능들을 경험해보세요!\n" +
                    "\n" +
                    "\uD83D\uDE46\uD83C\uDFFB\u200D♀️ 새로운 그룹을 만들면 초대 코드를 통해 친구들과 소중한 추억을 공유하실 수 있습니다 !";

            Party party = new Party(name, introduction);
            ChatRoom chatRoom = new ChatRoom(party);

            partyRepository.save(party);
            chatRoomRepository.save(chatRoom);

            party.updateChatRoom(chatRoom);
            partyRepository.save(party);

            oauthUtil.basicParty(member);
        }

    }
}
