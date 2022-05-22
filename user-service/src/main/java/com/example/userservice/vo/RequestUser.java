package com.example.userservice.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestUser {
  @NotBlank(message = "Email cannot be null")
  @Size(min = 2, message = "Email not be less than two characters")
  @Email
  private String email;

  @NotBlank(message = "Name cannot be null")
  @Size(min = 2, message = "Name not be less than two characters")
  private String name;

  @NotBlank(message = "Password cannot be null")
  @Size(min = 8, message = "Password must be equal or grater than 8 characters")
  private String pwd;
}
