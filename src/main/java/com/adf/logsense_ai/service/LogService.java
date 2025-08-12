package com.adf.logsense_ai.service;

import com.adf.logsense_ai.model.ErrorRequest;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final AiAdapter aiAdapter;
    private final EmailService emailService;

    public LogService(AiAdapter aiAdapter, EmailService emailService) {
        this.aiAdapter = aiAdapter;
        this.emailService = emailService;
    }

    public void processAndSendLog(ErrorRequest request) {
        String rawLog = request.getRawLog();
        String recipient = request.getRecipientEmail();

        String aiResponse = aiAdapter.explainError(rawLog);
        System.out.println("🧠 AI Response: " + aiResponse);

        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            aiResponse = """
                    [⚠️ AI Explanation Missing]

                    We attempted to analyze your error log but did not receive a valid response.
                    Please ensure the AI container (Ollama) is running and the model is available.
                    
                    Logs:
                    --------------------
                    """ + rawLog;
        }

        emailService.sendEmail(recipient, aiResponse);
    }
}
