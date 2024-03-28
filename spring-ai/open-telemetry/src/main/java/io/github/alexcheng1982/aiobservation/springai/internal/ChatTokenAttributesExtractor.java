package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import java.util.Optional;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

enum ChatTokenAttributesExtractor implements
    AttributesExtractor<Prompt, ChatResponse> {

  INSTANCE;

  @Override
  public void onStart(AttributesBuilder attributes, Context parentContext,
      Prompt prompt) {

  }

  @Override
  public void onEnd(AttributesBuilder attributes, Context context,
      Prompt prompt,
      ChatResponse chatResponse, Throwable error) {
    Optional.ofNullable(chatResponse)
        .flatMap(response -> Optional.ofNullable(response.getMetadata()))
        .flatMap(metadata -> Optional.ofNullable(metadata.getUsage()))
        .ifPresent(usage -> {
          AttributesUtils.internalSet(attributes,
              SemanticAttributes.PROMPT_TOKENS_COUNT,
              usage.getPromptTokens());
          AttributesUtils.internalSet(attributes,
              SemanticAttributes.GENERATION_TOKENS_COUNT,
              usage.getGenerationTokens());
          AttributesUtils.internalSet(attributes,
              SemanticAttributes.TOTAL_TOKENS_COUNT,
              usage.getTotalTokens());
        });
  }

}
