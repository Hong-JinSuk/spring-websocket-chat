package demo.socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.socket.domain.ChatMessageDTO;
import demo.socket.domain.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();

        chatRooms.put(roomId, chatRoom);
        log.info("Success Create Room : " + name);
        return chatRoom;
    }

    public void deleteRoom(String roomId) {
        ChatRoom chatRoom = findRoomById(roomId);
        // 방에 아무도 없다면 삭제
        if(chatRoom.getSessions().size() == 0) {
            chatRooms.remove(roomId);
            log.info("success delete roomId : " + roomId);
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


}
