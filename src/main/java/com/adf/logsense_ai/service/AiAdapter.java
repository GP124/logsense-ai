package com.adf.logsense_ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AiAdapter {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String explainError(String rawLog) {
        try {
            String shortLog = rawLog.length() > 2000 ? rawLog.substring(0, 2000) : rawLog;

            String prompt = """
            You are a developer assistant. Read this Java Spring Boot error log and generate a clear, human-readable explanation.

            Provide the result in this format:
            - **Error Summary**
            - **Root Cause**
            - **Suggestions**
            - **Reference Links** (if any)

            Avoid repeating full stack traces. Focus on useful parts like exception name, message, and likely fixes.

            Here is the log:
            """ + shortLog;

            String jsonPayload = mapper.createObjectNode()
                    .put("model", "gemma:2b-instruct")
                    .put("stream", false)
                    .put("prompt", prompt)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://ollama:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            System.out.println("🧠 Raw AI JSON: " + root.toPrettyString());

            return root.has("response") ? root.get("response").asText() : "⚠️ No 'response' field found. Full body: " + response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to explain log due to: " + e.getClass().getSimpleName() +
                    (e.getMessage() != null ? " - " + e.getMessage() : " (No message)");
        }
    }
}
