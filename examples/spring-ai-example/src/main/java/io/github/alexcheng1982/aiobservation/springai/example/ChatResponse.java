package io.github.alexcheng1982.aiobservation.springai.example;

import jakarta.annotation.Nullable;

public record ChatResponse(@Nullable String message, boolean success,
                           @Nullable String error) {

  public static ChatResponse success(String message) {
    return new ChatResponse(message, true, null);
  }

  public static ChatResponse error(String error) {
    return new ChatResponse(null, false, error);
  }
}
