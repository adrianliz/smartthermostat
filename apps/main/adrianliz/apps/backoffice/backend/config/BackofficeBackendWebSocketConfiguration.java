package adrianliz.apps.backoffice.backend.config;

import adrianliz.shared.infrastructure.config.Parameter;
import adrianliz.shared.infrastructure.config.ParameterNotExist;
import adrianliz.shared.infrastructure.spring.AssignPrincipalHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class BackofficeBackendWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
  final Parameter config;

  public BackofficeBackendWebSocketConfiguration(final Parameter config) {
    this.config = config;
  }

  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    try {
      registry
          .addEndpoint(config.get("BACKOFFICE_STOMP_ENDPOINT"))
          .setHandshakeHandler(new AssignPrincipalHandshakeHandler())
          .setAllowedOriginPatterns("*")
          .withSockJS();
    } catch (final ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void configureMessageBroker(final MessageBrokerRegistry registry) {
    try {
      registry.enableSimpleBroker(config.get("BACKOFFICE_TEMPERATURES_STOMP_TOPIC"));
      registry.setApplicationDestinationPrefixes(config.get("BACKOFFICE_STOMP_APP_PREFIX"));
      registry.setUserDestinationPrefix(config.get("BACKOFFICE_STOMP_USER_PREFIX"));
    } catch (final ParameterNotExist ex) {
      ex.printStackTrace();
    }
  }
}
