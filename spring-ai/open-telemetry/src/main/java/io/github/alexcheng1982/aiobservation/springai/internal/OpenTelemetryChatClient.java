package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public final class OpenTelemetryChatClient implements ChatClient {

  private final ChatClient client;
  private final Instrumenter<Prompt, ChatResponse> instrumenter;

  public OpenTelemetryChatClient(ChatClient client,
      Instrumenter<Prompt, ChatResponse> instrumenter) {
    this.client = client;
    this.instrumenter = instrumenter;
  }

  @Override
  public ChatResponse call(Prompt prompt) {
    Context parentContext = Context.current();
    if (!instrumenter.shouldStart(parentContext, prompt)) {
      return client.call(prompt);
    }
    ChatResponse response = null;
    Throwable error = null;
    Context context = instrumenter.start(parentContext, prompt);
    try (Scope ignore = context.makeCurrent()) {
      response = client.call(prompt);
    } catch (Throwable throwable) {
      error = throwable;
      throw throwable;
    } finally {
      instrumenter.end(context, prompt, response, error);
    }

    return response;
  }
}
