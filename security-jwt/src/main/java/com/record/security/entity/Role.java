package com.record.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.record.security.entity.Permission.*;

@RequiredArgsConstructor
public enum Role {

  // 用户
  USER(Collections.emptySet()),

  // 一线人员
  CHASER(
          Set.of(
                  CHASER_READ,
                  CHASER_UPDATE,
                  CHASER_CREATE,
                  CHASER_DELETE
          )
  ),

  // 部门主管
  SUPERVISOR(
          Set.of(
                  CHASER_READ,
                  CHASER_UPDATE,
                  CHASER_CREATE,
                  CHASER_DELETE,
                  SUPERVISOR_READ,
                  SUPERVISOR_UPDATE,
                  SUPERVISOR_CREATE,
                  SUPERVISOR_DELETE
          )
  ),

  // 系统管理员
  ADMIN(
          Set.of(
                  CHASER_READ,
                  CHASER_UPDATE,
                  CHASER_CREATE,
                  CHASER_DELETE,
                  SUPERVISOR_READ,
                  SUPERVISOR_UPDATE,
                  SUPERVISOR_CREATE,
                  SUPERVISOR_DELETE,
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE
          )
  ),

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
