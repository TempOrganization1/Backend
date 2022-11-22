package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.InvitationCodeRequestDto;
import com.sparta.actualpractice.dto.request.InvitationRequestDto;
import com.sparta.actualpractice.dto.response.InvitationResponseDto;
import com.sparta.actualpractice.entity.Invitation;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.MemberParty;
import com.sparta.actualpractice.entity.Party;
import com.sparta.actualpractice.repository.InvitationRepository;
import com.sparta.actualpractice.repository.MemberPartyRepository;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;

    private final MemberRepository memberRepository;


    public ResponseEntity<?> createCode(Long partyId, InvitationRequestDto invitationRequestDto, Member member) {

        // 받아온 partyId값 확인
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 없습니다."));
        // 로그인 한 유저가 그룹에 속해있는 경우만 초대 코드를 발급할 수 있음.
        MemberParty memberParty = memberPartyRepository.findByMemberAndParty(member, party).orElseThrow(() -> new NullPointerException("그룹에 초대할 수 있는 권한이 없습니다."));
        // 초대받는 사람 (받은 이메일) 찾아서 member1에 저장
        Member member1 = memberRepository.findByEmail(invitationRequestDto.getEmail()).orElseThrow(() -> new NullPointerException("해당 유저가 존재하지 않습니다."));

        if(member1.equals(member))
            throw new IllegalArgumentException("자신을 초대할 수 없습니다.");

        Invitation invitation;

        // 만약 이미 초대코드를 발급해놓은게 있다면?
        if ( invitationRepository.existsByMemberAndParty(member1, party)) {

            invitation = invitationRepository.findByMemberAndParty(member1, party).orElseThrow(() -> new NullPointerException("?"));

        } else {
            // 숫자,영문 8자리 난수 코드
            String invitationCode = createCode();

            while (invitationRepository.existsByCode(invitationCode)) {

                invitationCode = createCode();
            }

            invitation = new Invitation(invitationCode, member1, party);

            invitationRepository.save(invitation);
        }


        return new ResponseEntity<> ( new InvitationResponseDto(invitation), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> registerCode(InvitationCodeRequestDto invitationCodeRequestDto, Member member) {

        Invitation invitation = invitationRepository.findByCode(invitationCodeRequestDto.getCode()).orElseThrow(() -> new NullPointerException("유효하지 않은 코드입니다."));

        MemberParty memberParty = new MemberParty(member, invitation.getParty());

        memberPartyRepository.save(memberParty);
        invitationRepository.delete(invitation);

        return new ResponseEntity<>("그룹에 참여되었습니다.", HttpStatus.OK);
    }

    public String createCode() {

        Random random = new Random();
        StringBuffer code = new StringBuffer();

        for (int i = 0; i < 8; i++) {

            random.nextBoolean();

            if (random.nextBoolean())
                code.append((char) ((int) (random.nextInt(26)) + 97));
            else
                code.append((random.nextInt(10)));
        }

        return code.toString();
    }



}
