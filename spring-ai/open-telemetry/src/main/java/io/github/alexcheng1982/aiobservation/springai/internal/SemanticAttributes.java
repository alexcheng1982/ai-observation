package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributeKey;

public class SemanticAttributes {

  public static AttributeKey<Long> PROMPT_TOKENS_COUNT = AttributeKey.longKey("prompt_tokens");
  public static AttributeKey<Long> GENERATION_TOKENS_COUNT = AttributeKey.longKey(
      "generation_tokens");
  public static AttributeKey<Long> TOTAL_TOKENS_COUNT = AttributeKey.longKey("total_tokens");

  private SemanticAttributes() {
  }
}
