package demo.socket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demo.socket.service.ChatService;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class ChatRoom {
    private String roomId;
    private String name;
    // 채팅방이 갖고 있는 세션들 => 클라이언트를 알기위해서
//    @JsonIgnore
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handlerAction(WebSocketSession session, ChatMessageDTO chatMessage, ChatService service) {

        /**
         * type 확인 후, DTO와 맞고, ENTER라면 입장, TALK라면 채팅
         * 여기서 메세지를 저장하여 보여줄 수 있을 듯.
         */
        if (chatMessage.getType().equals(ChatMessageDTO.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + " has entered");
            sendMessage(chatMessage, service);
        } else if (chatMessage.getType().equals(ChatMessageDTO.MessageType.TALK)) {
            chatMessage.setMessage(chatMessage.getMessage());
            sendMessage(chatMessage, service);
        } else {
            log.info("[[[ ERROR!! MessageType is Wrong!! ]]]");
        }
    }

    private <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream()
                .forEach(session -> service.sendMessage(session, message));
    }
}
