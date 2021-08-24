package BoardExample.board.controller;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.service.BoardMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final BoardMemberMapper boardMemberMapper;
    private final BoardMemberService boardMemberService;

    @GetMapping("/login")
    public String loginForm() {
        return "/members/login";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "/members/join";
    }

    @PostMapping("/join")
    public String join(BoardMember member, Model model){
        log.info("## " +  member + " ##");
        int result = boardMemberService.join(member);

        if (result == -1){
            String msg = "해당 ID는 이미 사용중입니다.";
            model.addAttribute("msg", msg);
            return "/members/join";
        }
        return "redirect:/members/login";
    }

}
