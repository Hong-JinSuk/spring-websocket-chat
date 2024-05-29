package demo.socket.service;

import demo.socket.domain.ChatRoomStomp;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Service
public class ChatRepository {
    private Map<String, ChatRoomStomp> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChatRoomStomp> findAllRoom() {
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoomStomp findRoomById(String roomId) {
        return chatRoomMap.get(roomId);
    }

    public ChatRoomStomp createRoom(String name) {
        ChatRoomStomp chatRoom = new ChatRoomStomp().create(name); // 채팅방 생성
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void plusUserCnt(String roomId) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
    }

    public void minusUserCnt(String roomId) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
    }

    // 이 부분은 유저이름말고 userId를 받을 수도 있음. 일단 입장시 유저네임으로 입장을 가정함.
    public String addUser(String roomId, String username) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);
        String userId = UUID.randomUUID().toString();

        chatRoom.getUserlist().put(userId, username);
        return userId;
    }

    // 채팅방 유저 이름 중복 확인. (만약 로그인 시의 이름을 쓴다면 필요없는 기능)
    public String isDuplicateName(String roomId, String username) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);

        String tempUsername = username;
        while (chatRoom.getUserlist().containsValue(username)) {
            tempUsername = username + (int)(Math.random()*1000);
        }

        return tempUsername;
    }

    public void delUser(String roomId, String userId) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);
        chatRoom.getUserlist().remove(userId);
    }

    public String getUserName(String roomId, String userId) {
        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);
        return chatRoom.getUserlist().get(userId);
    }

    public ArrayList<String> getUserList(String roomId) {
        ArrayList<String> list = new ArrayList<>();

        ChatRoomStomp chatRoom = chatRoomMap.get(roomId);

        // key, value 값을 가져와서 value 값만 list에 반환하여 return
        chatRoom.getUserlist().forEach((key, value) -> list.add(value));
        return list;
    }

}
