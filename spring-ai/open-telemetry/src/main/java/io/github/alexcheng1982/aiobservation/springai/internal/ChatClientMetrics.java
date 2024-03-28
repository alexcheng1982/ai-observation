package io.github.alexcheng1982.aiobservation.springai.internal;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.DoubleHistogramBuilder;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import io.opentelemetry.instrumentation.api.instrumenter.OperationListener;
import io.opentelemetry.instrumentation.api.instrumenter.OperationMetrics;
import io.opentelemetry.instrumentation.api.internal.OperationMetricsUtil;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ChatClientMetrics implements OperationListener {

  private static final double NANOS_PER_S = TimeUnit.SECONDS.toNanos(1);

  private static final ContextKey<State> CHAT_CLIENT_CALL_METRICS_STATE =
      ContextKey.named("chat-client-metrics-state");

  public static OperationMetrics get() {
    return OperationMetricsUtil.create("chat client", ChatClientMetrics::new);
  }

  static final List<Double> DURATION_SECONDS_BUCKETS =
      List.of(0.005, 0.01, 0.025, 0.05, 0.075, 0.1, 0.25, 0.5, 0.75, 1.0, 2.5,
          5.0, 7.5, 10.0);

  private final DoubleHistogram duration;
  private final LongCounter promptTokenCounter;
  private final LongCounter generationTokenCounter;
  private final LongCounter totalTokenCounter;

  private ChatClientMetrics(Meter meter) {
    DoubleHistogramBuilder stableDurationBuilder =
        meter
            .histogramBuilder("chat.client.call.duration")
            .setUnit("s")
            .setDescription("Duration of ChatClient call requests.")
            .setExplicitBucketBoundariesAdvice(DURATION_SECONDS_BUCKETS);
    duration = stableDurationBuilder.build();
    promptTokenCounter = meter.counterBuilder("chat.client.prompt.tokens.count")
        .setDescription("Count of ChatClient prompt tokens")
        .build();
    generationTokenCounter = meter.counterBuilder(
            "chat.client.generation.tokens.count")
        .setDescription("Count of ChatClient generation tokens")
        .build();
    totalTokenCounter = meter.counterBuilder("chat.client.total.tokens.count")
        .setDescription("Count of ChatClient total tokens")
        .build();
  }

  @Override
  public Context onStart(Context context, Attributes startAttributes,
      long startNanos) {
    return context.with(
        CHAT_CLIENT_CALL_METRICS_STATE,
        new State(startAttributes, startNanos));
  }

  @Override
  public void onEnd(Context context, Attributes endAttributes, long endNanos) {
    State state = context.get(CHAT_CLIENT_CALL_METRICS_STATE);

    if (state == null) {
      return;
    }

    Attributes attributes = state.startAttributes().toBuilder()
        .putAll(endAttributes)
        .build();

    recordCounter(attributes, SemanticAttributes.PROMPT_TOKENS_COUNT,
        promptTokenCounter);
    recordCounter(attributes, SemanticAttributes.GENERATION_TOKENS_COUNT,
        generationTokenCounter);
    recordCounter(attributes, SemanticAttributes.TOTAL_TOKENS_COUNT,
        totalTokenCounter);

    duration.record((endNanos - state.startTimeNanos()) / NANOS_PER_S,
        Attributes.empty(), context);
  }

  private void recordCounter(Attributes attributes,
      AttributeKey<Long> attributeKey, LongCounter counter) {
    Optional.ofNullable(attributes.get(attributeKey))
        .ifPresent(counter::add);
  }

  record State(Attributes startAttributes, long startTimeNanos) {

  }
}
