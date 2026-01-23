package com.Prakhar.Ecommerce.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1/")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader("HTTP-Referer", "http://localhost:8080") // required by OpenRouter
                .defaultHeader("X-Title", "Ecommerce AI Chatbot")
                .build();
    }

    public String chatReply(String userMessage) {

        Map<String, Object> body = Map.of(
                "model", "deepseek/deepseek-r1",
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content",
                                """
                                        Format every reply in clean MARKDOWN.
                                        Rules:
                                        - Use headings (##)
                                        - Use bullet points
                                        - Use bold text
                                        - Keep responses short
                                        - Do NOT include reasoning or internal thoughts
                                        """
                        ),
                        Map.of(
                                "role", "user",
                                "content", userMessage
                        )
                )
        );

        try {
            Mono<Map> resp = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class);

            Map result = resp.block();
            List choices = (List) result.get("choices");

            Map choice = (Map) choices.get(0);
            Map msg = (Map) choice.get("message");

            String content = msg.get("content").toString();

            // Remove deepseek thinking
            content = content.replaceAll("(?s)<think>.*?</think>", "");

            return content.trim();

        } catch (Exception e) {
            return "AI Error: " + e.getMessage();
        }
    }
}