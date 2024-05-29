package demo.socket.controller;

import demo.socket.domain.ChatRoom;
import demo.socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRooms() {
        return chatService.findAllRoom();
    }

    @DeleteMapping
    public void deleteRoom(@RequestParam String roomId) {
        chatService.deleteRoom(roomId);
    }

}
