package com.record.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chaser")
@Tag(name = "一线人员权限测试")
public class ChaserController {


    @Operation(
            description = "Get endpoint for chaser",
            summary = "This is a summary for chaser get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public String get() {
        return "GET |==| ChaserController";
    }

    @PostMapping
    public String post() {
        return "POST |==| ChaserController";
    }

    @PutMapping
    public String put() {
        return "PUT |==| ChaserController";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE |==| ChaserController";
    }
}
