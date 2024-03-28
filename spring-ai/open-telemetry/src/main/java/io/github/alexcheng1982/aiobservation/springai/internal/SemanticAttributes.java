package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributeKey;

public class SemanticAttributes {

  public static AttributeKey<Long> PROMPT_TOKENS_COUNT = AttributeKey.longKey(
      "ai.chat.prompt.tokens");
  public static AttributeKey<Long> GENERATION_TOKENS_COUNT = AttributeKey.longKey(
      "ai.chat.generation.tokens");
  public static AttributeKey<Long> TOTAL_TOKENS_COUNT = AttributeKey.longKey(
      "ai.chat.total.tokens");

  public static AttributeKey<String> PROMPT_MESSAGES = AttributeKey.stringKey(
      "ai.chat.prompt.messages");

  public static AttributeKey<String> CHAT_RESPONSE_MESSAGES = AttributeKey.stringKey(
      "ai.chat.response.messages");

  private SemanticAttributes() {
  }
}
