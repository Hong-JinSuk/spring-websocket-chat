package demo.socket.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ChatRoomStomp {

    private String roomId;
    private String roomName;
    private long userCount;

    private HashMap<String, String> userlist = new HashMap<String,String>();

    public ChatRoomStomp create(String name) {
        ChatRoomStomp chatRoom = new ChatRoomStomp();
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setRoomName(name);

        return chatRoom;
    }
}
