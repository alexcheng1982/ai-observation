package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.instrumentation.api.instrumenter.SpanNameExtractor;
import org.springframework.ai.chat.prompt.Prompt;

class ChatClientSpanNameExtractor implements SpanNameExtractor<Prompt> {

  @Override
  public String extract(Prompt prompt) {
    return "chat";
  }
}
