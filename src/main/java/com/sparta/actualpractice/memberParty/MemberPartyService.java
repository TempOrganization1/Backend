package com.sparta.actualpractice.memberParty;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.memberParty.MemberPartyRepository;
import com.sparta.actualpractice.party.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPartyService {

    private final MemberPartyRepository memberPartyRepository;

    private final PartyRepository partyRepository;

    @Transactional
    public ResponseEntity<?> createMemberParty(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(()-> new NullPointerException("해당 그룹이 없습니다."));

        if (memberPartyRepository.existsByMemberAndParty(member, party)) {
            memberPartyRepository.deleteByPartyAndMember(party, member);

            return new ResponseEntity<>("해당 그룹을 나왔습니다.", HttpStatus.OK);
        }
        else {
            memberPartyRepository.save(new MemberParty(member, party));

            return new ResponseEntity<>("해당 그룹에 참가하였습니다.", HttpStatus.OK);
        }
    }
}
