package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.instrumentation.api.instrumenter.SpanStatusBuilder;
import io.opentelemetry.instrumentation.api.instrumenter.SpanStatusExtractor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

class ChatClientSpanStatusExtractor implements
    SpanStatusExtractor<Prompt, ChatResponse> {

  @Override
  public void extract(SpanStatusBuilder spanStatusBuilder, Prompt prompt,
      ChatResponse chatResponse, Throwable error) {
    spanStatusBuilder.setStatus(
        error == null ? StatusCode.OK : StatusCode.ERROR);
  }
}
