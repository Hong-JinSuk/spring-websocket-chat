package demo.socket.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ChatMessageDTO {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
//    private String time;

    public ChatMessageDTO() {

    }

}
