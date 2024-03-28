package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;
import io.opentelemetry.instrumentation.api.instrumenter.SpanKindExtractor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public class ChatClientInstrumenterFactory {

  private static final String INSTRUMENTATION_NAME = "io.github.alexcheng1982.springai";

  public static Instrumenter<Prompt, ChatResponse> createInstrumenter(
      OpenTelemetry openTelemetry) {

    InstrumenterBuilder<Prompt, ChatResponse> builder =
        Instrumenter.<Prompt, ChatResponse>builder(
                openTelemetry, INSTRUMENTATION_NAME, new ChatClientSpanNameExtractor())
            .setSpanStatusExtractor(new ChatClientSpanStatusExtractor())
            .addAttributesExtractor(new ChatClientAttributesExtractor())
            .addOperationMetrics(ChatClientMetrics.get());
    return builder.buildInstrumenter(SpanKindExtractor.alwaysClient());
  }

  private ChatClientInstrumenterFactory() {
  }
}
