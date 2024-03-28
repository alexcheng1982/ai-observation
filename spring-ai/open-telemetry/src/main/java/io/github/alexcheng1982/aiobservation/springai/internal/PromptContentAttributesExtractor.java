package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public enum PromptContentAttributesExtractor implements
    AttributesExtractor<Prompt, ChatResponse> {
  INSTANCE;

  @Override
  public void onStart(AttributesBuilder attributes, Context parentContext,
      Prompt prompt) {
    AttributesUtils.internalSet(attributes, SemanticAttributes.PROMPT_MESSAGES,
        prompt.getContents());
  }

  @Override
  public void onEnd(AttributesBuilder attributes, Context context,
      Prompt prompt, ChatResponse chatResponse, Throwable error) {

  }
}
