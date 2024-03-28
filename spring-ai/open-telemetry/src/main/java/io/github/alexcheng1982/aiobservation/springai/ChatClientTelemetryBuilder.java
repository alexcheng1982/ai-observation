package io.github.alexcheng1982.aiobservation.springai;

import io.github.alexcheng1982.aiobservation.springai.internal.ChatClientInstrumenterFactory;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public class ChatClientTelemetryBuilder {

  private final OpenTelemetry openTelemetry;

  public ChatClientTelemetryBuilder(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;
  }

  public ChatClientTelemetry build() {
    Instrumenter<Prompt, ChatResponse> instrumenter =
        ChatClientInstrumenterFactory.createInstrumenter(
            openTelemetry);

    return new ChatClientTelemetry(instrumenter);
  }
}
