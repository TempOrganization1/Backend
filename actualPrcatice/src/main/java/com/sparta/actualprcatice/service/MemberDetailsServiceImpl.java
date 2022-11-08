package com.sparta.actualprcatice.service;

import com.example.consolelog.entity.Member;
import com.example.consolelog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        Member member = memberRepository.findByName(nickname).orElseThrow(() -> new UsernameNotFoundException("Can't find " + nickname));

        return new MemberDetailsImpl(member);
    }
}
