package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import io.opentelemetry.instrumentation.api.instrumenter.InstrumenterBuilder;
import io.opentelemetry.instrumentation.api.instrumenter.SpanKindExtractor;
import java.util.List;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public class ChatClientInstrumenterFactory {

  private static final String INSTRUMENTATION_NAME = "io.github.alexcheng1982.ai-observation";

  public static Instrumenter<Prompt, ChatResponse> createInstrumenter(
      OpenTelemetry openTelemetry,
      List<AttributesExtractor<? super Prompt, ? super ChatResponse>> additionalExtractors) {

    InstrumenterBuilder<Prompt, ChatResponse> builder =
        Instrumenter.<Prompt, ChatResponse>builder(
                openTelemetry, INSTRUMENTATION_NAME,
                new ChatClientSpanNameExtractor())
            .setSpanStatusExtractor(new ChatClientSpanStatusExtractor())
            .addAttributesExtractor(ChatTokenAttributesExtractor.INSTANCE)
            .addAttributesExtractors(additionalExtractors)
            .addOperationMetrics(ChatClientMetrics.get());
    return builder.buildInstrumenter(SpanKindExtractor.alwaysClient());
  }

  private ChatClientInstrumenterFactory() {
  }
}
