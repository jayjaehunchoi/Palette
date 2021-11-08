package com.palette.dto;


import com.palette.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class MemberDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

   public Member toEntity() {
       Member build = Member.builder()
               .name(name)
               .password(password)
               .email(email)
               .build();
       return build;
   }

   @Builder
   public MemberDto(String name, String password, String email) {
       this.name = name;
       this.password = password;
       this.email = email;
   }
}