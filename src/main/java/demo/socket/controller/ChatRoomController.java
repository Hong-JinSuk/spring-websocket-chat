package demo.socket.controller;

import demo.socket.domain.ChatRoomStomp;
import demo.socket.service.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    @Autowired
    private ChatRepository chatRepository;

    // goChatRoom 사용시 roomList 를 받아옴. 메소드 이름은 getRoomList 로 바꾸는게 좋을듯.
    @GetMapping
    public String goChatRoom(Model model) {
        model.addAttribute("list", chatRepository.findAllRoom());

        log.info("[ RoomList ] : {}", chatRepository.findAllRoom());
        return "roomlist";
    }

    // createRoom 이라는 버튼 클릭시 채팅룸 생성.
    @PostMapping("/createRoom")
    public String createRoom(@RequestParam String name, RedirectAttributes attributes) {
        ChatRoomStomp chatRoom = chatRepository.createRoom(name);
        log.info("[ Create ChatRoom ] : {}", chatRoom);

        attributes.addFlashAttribute("[ The name of ChatRoom ] : {}", name);
        return "redirect:/";
    }

    // 채팅방의 아이디를 받아 클라이언트를 입장시킨다. 메소드 이름은 EnterRoom 으로 바꾸는게 좋을듯.
    @GetMapping("/room")
    public String roomDetail(Model model, String roomId) {
        log.info("roomId : {}", roomId);
        model.addAttribute("room", chatRepository.findRoomById(roomId));
        return "chatroom";
    }
}
