package com.Prakhar.Ecommerce.Service;

import com.Prakhar.Ecommerce.LLMTools.OrderTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiService {

    private final ChatClient chatClient;

    public SpringAiService(ChatClient.Builder builder,
                           OrderTool orderTool) {

        this.chatClient = builder
                .defaultTools(orderTool)
                .build();
    }

    public String chat(String message) {

        return chatClient.prompt()
                .system("""
You are an ecommerce support assistant.
Never create fake orders.
For order, payment, email, status or razorpay related questions,
always use available tools.
If no data exists, say data not found in database.
""")
                .user(message)
                .call()
                .content();
    }
}