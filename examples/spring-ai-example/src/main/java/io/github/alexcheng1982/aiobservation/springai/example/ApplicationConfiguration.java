package io.github.alexcheng1982.aiobservation.springai.example;

import io.github.alexcheng1982.aiobservation.springai.ChatClientTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.util.Optional;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ApplicationConfiguration {

  @Bean
  @Primary
  public ChatClient chatClient(OllamaChatClient ollamaChatClient,
      OpenTelemetry openTelemetry) {
    return ChatClientTelemetry.builder(openTelemetry)
        .tracePromptContent(true)
        .traceChatResponseContent(true)
        .build()
        .newChatClient(ollamaChatClient);
  }

  @Bean
  public OpenTelemetry openTelemetry(ApplicationContext applicationContext) {
    return AutoConfiguredOpenTelemetrySdk.builder()
        .addResourceCustomizer(
            ((resource, configProperties) -> resource.toBuilder()
                .put(AttributeKey.stringKey("service.name"),
                    Optional.ofNullable(applicationContext.getId())
                        .orElseGet(applicationContext::getDisplayName))
                .build()))
        .setResultAsGlobal()
        .build()
        .getOpenTelemetrySdk();
  }
}
