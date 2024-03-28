package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public enum ChatResponseContentAttributesExtractor implements
    AttributesExtractor<Prompt, ChatResponse> {
  INSTANCE;

  @Override
  public void onStart(AttributesBuilder attributes, Context parentContext,
      Prompt prompt) {

  }

  @Override
  public void onEnd(AttributesBuilder attributes, Context context,
      Prompt prompt, ChatResponse chatResponse, Throwable error) {
    if (chatResponse == null) {
      return;
    }
    var messages = chatResponse.getResults().stream()
        .flatMap(
            result -> Optional.ofNullable(result.getOutput())
                .flatMap(message -> Optional.ofNullable(message.getContent()))
                .stream()
        )
        .map(String::trim)
        .filter(str -> !str.isEmpty())
        .collect(Collectors.joining("\n"));
    AttributesUtils.internalSet(attributes,
        SemanticAttributes.CHAT_RESPONSE_MESSAGES, messages);
  }
}
