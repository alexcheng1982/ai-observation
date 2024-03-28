package io.github.alexcheng1982.aiobservation.springai;

import io.github.alexcheng1982.aiobservation.springai.internal.OpenTelemetryChatClient;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * {@linkplain ChatClient} telemetry
 */
public final class ChatClientTelemetry {

  public static ChatClientTelemetry create(OpenTelemetry openTelemetry) {
    return builder(openTelemetry).build();
  }

  public static ChatClientTelemetryBuilder builder(
      OpenTelemetry openTelemetry) {
    return new ChatClientTelemetryBuilder(openTelemetry);
  }

  private final Instrumenter<Prompt, ChatResponse> instrumenter;

  public ChatClientTelemetry(Instrumenter<Prompt, ChatResponse> instrumenter) {
    this.instrumenter = instrumenter;
  }

  /**
   * Wrap a {@linkplain ChatClient} to support telemetry
   *
   * @param chatClient
   * @return
   */
  public ChatClient newChatClient(ChatClient chatClient) {
    return new OpenTelemetryChatClient(chatClient, instrumenter);
  }
}
