package com.record.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    // 一线人员
    CHASER_READ("chaser:read"),
    CHASER_UPDATE("chaser:update"),
    CHASER_CREATE("chaser:create"),
    CHASER_DELETE("chaser:delete"),

    // 部门主管
    SUPERVISOR_READ("supervisor:read"),
    SUPERVISOR_UPDATE("supervisor:update"),
    SUPERVISOR_CREATE("supervisor:create"),
    SUPERVISOR_DELETE("supervisor:delete"),

    // 系统管理员
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    ;

    @Getter
    private final String permission;
}
