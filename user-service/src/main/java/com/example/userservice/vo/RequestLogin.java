package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestLogin {

  @NotBlank(message = "이메일은 비어있을 수 없습니다.")
  @Size(min = 2, message = "이메일은 2글자 이상입니다.")
  @Email
  private String email;

  @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
  @Size(min = 8, message = "비밀번호는 8글자 이상입니다.")

  private String password;

}
