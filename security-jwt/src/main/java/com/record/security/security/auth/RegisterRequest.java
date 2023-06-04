package com.record.security.security.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @Schema(description = "昵称")
  private String nickname;
  @Schema(description = "邮箱")
  private String email;
  @Schema(description = "密码")
  private String password;
  @Schema(description = "角色，可选值: CHASER, SUPERVISOR, ADMIN")
  private String role;
}
