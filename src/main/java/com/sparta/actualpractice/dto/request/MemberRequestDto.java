package com.sparta.actualpractice.dto.request;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import javax.validation.constraints.NotBlank;

@Getter
public class MemberRequestDto {

    @NotBlank
//    @Pattern(regexp = "^(?=.*[a-zA-Z])[-a-zA-Z0-9]{0,4}$")  // ㅈ정규식 바꿔야됌
    private String name;

    @NotBlank
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{8,20}$")
    private String password;

    @NotBlank
    private String email;

    public UsernamePasswordAuthenticationToken toAuthentication() {

        return new UsernamePasswordAuthenticationToken(email, password);
    }

}

