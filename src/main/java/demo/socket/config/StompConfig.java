package demo.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // stomp의 연결주소는 /ws-stomp
        registry.addEndpoint("/ws-stomp")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /sub 로 도착하는 것은 구독
        config.enableSimpleBroker("/sub");
        // /pub 로 도착하는 것은 메세지를 송수신할 때, 사용하는 엔드포인트
        config.setApplicationDestinationPrefixes("/pub");
    }
}
