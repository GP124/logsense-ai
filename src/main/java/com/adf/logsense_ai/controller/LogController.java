package com.adf.logsense_ai.controller;

import com.adf.logsense_ai.model.ErrorRequest;
import com.adf.logsense_ai.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<String> processLog(@RequestBody ErrorRequest request) {
        logService.processAndSendLog(request);
        return ResponseEntity.ok("✅ Log processed and emailed successfully!");
    }
}