package demo.socket.controller;

import demo.socket.domain.ChatMessageDTO;
import demo.socket.service.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatControllerStomp {

    private final SimpMessageSendingOperations template;

    @Autowired
    ChatRepository repository;

    /**
     * @MessageMapping을 통해서 WebSocket 으로 들어오는 메세지를 발신 처리한다.
     * 클라이언트는 /pub/chat/message 로 요청하게되고, 이것을 controller 가 받아서 처리한다.
     * 처리가 완료되면 /sub/chat/room/roomId 로 메세지가 전송된다.
     */
    @MessageMapping("/enterUser")
    public void enterUser(@Payload ChatMessageDTO chat, SimpMessageHeaderAccessor headerAccessor) {
        repository.plusUserCnt(chat.getRoomId());
        String userId = repository.addUser(chat.getRoomId(), chat.getSender());


        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " has entered");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

    @GetMapping("/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return repository.getUserList(roomId);
    }

    @GetMapping("/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam String roomId, @RequestParam String username) {
        String userName = repository.isDuplicateName(roomId, username);
        log.info("생성된 유저 이름 : {}", userName);

        return userName;
    }
}
