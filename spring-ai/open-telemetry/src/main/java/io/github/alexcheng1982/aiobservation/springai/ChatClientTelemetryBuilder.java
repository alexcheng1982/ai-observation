package io.github.alexcheng1982.aiobservation.springai;

import io.github.alexcheng1982.aiobservation.springai.internal.ChatClientInstrumenterFactory;
import io.github.alexcheng1982.aiobservation.springai.internal.ChatResponseContentAttributesExtractor;
import io.github.alexcheng1982.aiobservation.springai.internal.PromptContentAttributesExtractor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public class ChatClientTelemetryBuilder {

  private final OpenTelemetry openTelemetry;
  private final List<AttributesExtractor<? super Prompt, ? super ChatResponse>> additionalExtractors = new ArrayList<>();

  public ChatClientTelemetryBuilder(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;
  }

  public ChatClientTelemetryBuilder tracePromptContent(boolean shouldTrace) {
    if (shouldTrace) {
      additionalExtractors.add(PromptContentAttributesExtractor.INSTANCE);
    } else {
      additionalExtractors.remove(PromptContentAttributesExtractor.INSTANCE);
    }
    return this;
  }

  public ChatClientTelemetryBuilder traceChatResponseContent(
      boolean shouldTrace) {
    if (shouldTrace) {
      additionalExtractors.add(ChatResponseContentAttributesExtractor.INSTANCE);
    } else {
      additionalExtractors.remove(
          ChatResponseContentAttributesExtractor.INSTANCE);
    }
    return this;
  }

  public ChatClientTelemetry build() {
    Instrumenter<Prompt, ChatResponse> instrumenter =
        ChatClientInstrumenterFactory.createInstrumenter(
            openTelemetry, additionalExtractors);

    return new ChatClientTelemetry(instrumenter);
  }
}
