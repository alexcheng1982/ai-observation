# Java AI App Observation

## Spring AI

### OpenTelemetry

#### Metrics

| Name                                  | Type      | Description                           |
|---------------------------------------|-----------|---------------------------------------|
| `chat.client.call.duration`           | Histogram | Duration of ChatClient call requests  |
| `chat.client.prompt.tokens.count`     | Counter   | Count of ChatClient prompt tokens     |
| `chat.client.generation.tokens.count` | Counter   | Count of ChatClient generation tokens |
| `chat.client.total.tokens.count`      | Counter   | Count of ChatClient total tokens      |

For `ChatClient`s, using `ChatClientTelemetry` to wrap an existing `ChatClient`.

```java

@Configuration
public class ApplicationConfiguration {

  @Bean
  @Primary
  public ChatClient chatClient(OllamaChatClient ollamaChatClient,
      OpenTelemetry openTelemetry) {
    return ChatClientTelemetry.builder(openTelemetry)
        .tracePromptContent(true) // Trace prompt content
        .traceChatResponseContent(true) // Trace chat response content
        .build()
        .newChatClient(ollamaChatClient);
  }

  @Bean
  public OpenTelemetry openTelemetry(ApplicationContext applicationContext) {
    return AutoConfiguredOpenTelemetrySdk.builder()
        .addResourceCustomizer(
            ((resource, configProperties) -> resource.toBuilder()
                .put(AttributeKey.stringKey("service.name"),
                    Optional.ofNullable(applicationContext.getId())
                        .orElseGet(applicationContext::getDisplayName))
                .build()))
        .setResultAsGlobal()
        .build()
        .getOpenTelemetrySdk();
  }
}
```