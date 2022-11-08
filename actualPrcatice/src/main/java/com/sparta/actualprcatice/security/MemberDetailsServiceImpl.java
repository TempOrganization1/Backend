package com.sparta.actualprcatice.security;


import com.sparta.actualprcatice.entity.Member;
import com.sparta.actualprcatice.repository.MemberRepository;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다 : " + email));

        return new MemberDetailsImpl(member);
    }
}
