package com.adf.logsense_ai.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("🚨 AI-Powered Error Log Insight");
            helper.setText(
                    String.format("""
                    <html>
                      <body style='font-family: sans-serif;'>
                        <h3>AI-Powered Log Explanation</h3>
                        <pre style='white-space: pre-wrap; background: #f4f4f4; padding: 1em; border-left: 4px solid #ccc;'>%s</pre>
                      </body>
                    </html>
                    """, content), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
