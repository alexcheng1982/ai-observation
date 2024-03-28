package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.instrumentation.api.instrumenter.SpanStatusBuilder;
import io.opentelemetry.instrumentation.api.instrumenter.SpanStatusExtractor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

class ChatClientSpanStatusExtractor implements
    SpanStatusExtractor<Prompt, ChatResponse> {

  @Override
  public void extract(SpanStatusBuilder spanStatusBuilder, Prompt prompt,
      ChatResponse chatResponse, Throwable error) {
    SpanStatusExtractor.getDefault().extract(spanStatusBuilder, prompt, chatResponse, error);
  }
}
