package com.sparta.actualprcatice.dto.requestDto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class MemberReqeustDto {
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])[-a-zA-Z0-9]{4,10}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$")
    private String password;

    @NotBlank
    private String nickname;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(name, password);
    }

}

