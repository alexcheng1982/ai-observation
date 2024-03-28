package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import java.util.Optional;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.lang.Nullable;

class ChatClientAttributesExtractor implements
    AttributesExtractor<Prompt, ChatResponse> {

  @Override
  public void onStart(AttributesBuilder attributes, Context parentContext, Prompt prompt) {

  }

  @Override
  public void onEnd(AttributesBuilder attributes, Context context, Prompt prompt,
      ChatResponse chatResponse, Throwable error) {
    Optional.ofNullable(chatResponse)
        .flatMap(response -> Optional.ofNullable(response.getMetadata()))
        .flatMap(metadata -> Optional.ofNullable(metadata.getUsage()))
        .ifPresent(usage -> {
          internalSet(attributes, SemanticAttributes.PROMPT_TOKENS_COUNT, usage.getPromptTokens());
          internalSet(attributes, SemanticAttributes.GENERATION_TOKENS_COUNT,
              usage.getGenerationTokens());
          internalSet(attributes, SemanticAttributes.TOTAL_TOKENS_COUNT, usage.getTotalTokens());
        });
  }

  <T> void internalSet(
      AttributesBuilder attributes, AttributeKey<T> key, @Nullable T value) {
    if (value != null) {
      attributes.put(key, value);
    }
  }
}
