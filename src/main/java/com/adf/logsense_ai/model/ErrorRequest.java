package com.adf.logsense_ai.model;

import lombok.Data;

@Data
public class ErrorRequest {
    private String rawLog;
    private String recipientEmail;
}
