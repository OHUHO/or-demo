package com.record.security.security.auth;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.record.security.entity.Role;
import com.record.security.entity.Token;
import com.record.security.entity.TokenType;
import com.record.security.entity.User;
import com.record.security.security.config.JwtService;
import com.record.security.service.TokenService;
import com.record.security.service.UserService;
import com.record.security.utils.Result;
import com.record.security.utils.TimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserService userService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  public Result register(RegisterRequest request) {
    if (StringUtils.isNoneBlank(request.getNickname()) &&
            StringUtils.isNoneBlank(request.getEmail()) &&
            StringUtils.isNoneBlank(request.getPassword()) &&
            StringUtils.isNoneBlank(request.getRole())){

      var oldUser = userService.findByEmail(request.getEmail());
      // DONE 判断用户是否存在
      if (oldUser == null){
        var user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .createTime(TimeUtils.generateCurrentTime())
                .build();
        // mybatis-plus 保存用户信息后自动返回递增的主键id
        userService.save(user);
        return Result.success("注册成功！");

      }
      // 用户存在，提示用户登录
      return Result.error("用户已存在！");

    }
    return Result.error("参数不能为空！");

  }

  public Result authenticate(AuthenticationRequest request) {

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

    if (userDetails == null || !passwordEncoder.matches(request.getPassword(), userDetails.getPassword())){
      return Result.error("用户名或密码不正确！");
    }

    Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    );
    // TODO 考虑不从数据库中查数据

    var user = (User) authenticate.getPrincipal();
    // var user = userService.findByEmail(request.getEmail());
    // var user = userDetailsService.loadUserByUsername(request.getEmail());


    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeUserToken(user.getId());
    saveUserToken(user.getId(), jwtToken);
    AuthenticationResponse response = AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
    return Result.success("认证成功！",response);

  }

  public Result refreshToken(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String username;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return Result.error("你的操作非法！");
    }
    refreshToken = authHeader.substring(7);
    username = jwtService.extractUsername(refreshToken);
    if (username != null) {
      // log.info("username: {}",username);
      // TODO 考虑不从数据库获取用户数据
      // var user = userService.findByEmail(username);
      var user = (User) userDetailsService.loadUserByUsername(username);
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        log.info("accessToken: {}",accessToken);
        revokeUserToken(user.getId());
        saveUserToken(user.getId(), accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        // new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        return Result.success("刷新成功！",authResponse);
      }
      return Result.error("令牌无效！");
    }
    return Result.error("获取用户名失败！");
  }


  /**
   * 保存用户的 token
   * @param userId
   * @param jwtToken
   */
  private void saveUserToken(Long userId, String jwtToken) {
    var token = Token.builder()
            .userId(userId)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    LambdaUpdateWrapper<Token> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
    lambdaUpdateWrapper.eq(Token::getUserId,userId);
    tokenService.saveOrUpdate(token,lambdaUpdateWrapper);
  }


  /**
   * 销毁用户的 token
   *   - expired = true
   *   - revoked = true
   * @param userId
   */
  private void revokeUserToken(Long userId) {
    var validUserToken = tokenService.findValidTokenByUserId(userId);
    if (validUserToken.isEmpty())
      return;
    validUserToken.ifPresent(token -> {
      token.setExpired(true);
      token.setRevoked(true);
      LambdaUpdateWrapper<Token> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
      lambdaUpdateWrapper.eq(Token::getUserId,userId);
      tokenService.update(token,lambdaUpdateWrapper);
    });

  }
}
