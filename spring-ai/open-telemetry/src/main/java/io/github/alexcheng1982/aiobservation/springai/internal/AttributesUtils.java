package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributesBuilder;
import org.springframework.lang.Nullable;

public class AttributesUtils {

  private AttributesUtils() {
  }

  static <T> void internalSet(
      AttributesBuilder attributes, AttributeKey<T> key, @Nullable T value) {
    if (value != null) {
      attributes.put(key, value);
    }
  }
}
