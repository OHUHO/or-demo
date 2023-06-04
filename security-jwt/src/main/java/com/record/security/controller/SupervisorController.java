package com.record.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/supervisor")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "部门主管权限测试")
public class SupervisorController {

    @GetMapping
    @PreAuthorize("hasAuthority('supervisor:read')")
    public String get() {
        return "GET |==| SupervisorController";
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('supervisor:create')")
    // @Hidden
    public String post() {
        return "POST |==| SupervisorController";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('supervisor:update')")
    // @Hidden
    public String put() {
        return "PUT |==| SupervisorController";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('supervisor:delete')")
    // @Hidden
    public String delete() {
        return "DELETE |==| SupervisorController";

    }
}