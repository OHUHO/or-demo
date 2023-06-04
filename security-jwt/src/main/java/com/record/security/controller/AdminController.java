package com.record.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "系统管理员权限测试")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET |==| AdminController";
    }


    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String post() {
        return "POST |==| AdminController";
    }


    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String put() {
        return "PUT |==| AdminController";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    // @Hidden
    public String delete() {
        return "DELETE |==| AdminController";
    }
}
