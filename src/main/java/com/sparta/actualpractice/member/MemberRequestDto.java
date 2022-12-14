package com.sparta.actualpractice.member;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class MemberRequestDto {

    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,4}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z!@#$%^&*]{4,12}$")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_\\\\-]+@[A-Za-z0-9-]+[.][A-Za-z]{1,3}$")
    private String email;

    public UsernamePasswordAuthenticationToken toAuthentication() {

        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
