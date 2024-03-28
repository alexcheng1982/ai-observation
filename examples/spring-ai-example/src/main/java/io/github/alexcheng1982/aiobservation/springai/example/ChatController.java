package io.github.alexcheng1982.aiobservation.springai.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private final ChatClient chatClient;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public ChatController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PostMapping("/chat")
  public ResponseEntity<ChatResponse> chat(ChatRequest request) {
    try {
      return ResponseEntity.ok(
          ChatResponse.success(chatClient.call(request.message())));
    } catch (Exception e) {
      logger.error("Chat error ", e);
      return ResponseEntity.ok(ChatResponse.error(e.getMessage()));
    }
  }
}
