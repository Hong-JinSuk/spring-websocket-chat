package demo.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.socket.domain.ChatMessageDTO;
import demo.socket.domain.ChatRoom;
import demo.socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private ChatRoom chatRoom;
    private String roomId;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connect Success WebSocket ");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // get message
        String payload = message.getPayload();
        log.info("payload {}", payload);

        // Mapper
        ChatMessageDTO chatMessageDTO = objectMapper.readValue(payload, ChatMessageDTO.class);
        log.info("session {}", chatMessageDTO.toString());

        // get UUID
        roomId = chatMessageDTO.getRoomId();

        // find chatRoom by UUID
        chatRoom = chatService.findRoomById(roomId);
        log.info("room {}", chatRoom.toString());

        // chatMessageDTO 의 type에 따라 로직 결정
        chatRoom.handlerAction(session, chatMessageDTO, chatService);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatRoom.getSessions().remove(session);
        chatService.deleteRoom(roomId);
        log.info("close WebSocket");
    }
}
