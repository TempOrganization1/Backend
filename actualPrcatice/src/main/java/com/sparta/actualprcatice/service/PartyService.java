package com.sparta.actualprcatice.service;

import com.sparta.actualprcatice.dto.request.PartyRequestDto;
import com.sparta.actualprcatice.entity.Admin;
import com.sparta.actualprcatice.entity.Member;
import com.sparta.actualprcatice.entity.Party;
import com.sparta.actualprcatice.repository.AdminRepository;
import com.sparta.actualprcatice.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;

    private final AdminRepository adminRepository;

    public ResponseEntity<?> createParty(PartyRequestDto partyRequestDto, Member member) {

        Party party = new Party(partyRequestDto);
        Admin admin = new Admin(member,party);

        partyRepository.save(party);
        adminRepository.save(admin);

        return new ResponseEntity<>("그룹이 생성되었습니다.", HttpStatus.OK);
    }



    public boolean validateMember(Member member, Party party) {

        boolean result = true;

        for(int i = 0; i < party.getMemberPartyList().size(); i++ ){
            if( !member.getEmail().equals(party.getMemberPartyList().get(i).getMember().getEmail())) {
                result = false;
            }
        }

        return result;
    }

}
