package BoardExample.board.controller;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.service.BoardMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final BoardMemberMapper boardMemberMapper;
    private final BoardMemberService boardMemberService;

    @GetMapping("/login")
    public String loginForm() {
        return "/account/login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        BoardMember member = boardMemberMapper.getById(username);

        //id가 DB에 존재하지 않으면,
        if (member == null) {
            String msg_id = "해당 ID는 존재하지 않습니다.";
            model.addAttribute("msg_id",msg_id);
            return "/account/login";
        }

        //id는 DB에 존재하지만, password가 일치하지 않으면,
        if (!password.equals(member.getPassword())) {
            String msg_pw = "Password가 일치하지 않습니다.";
            model.addAttribute("msg_pw", msg_pw);
            return "/account/login";
        }

        //id와 password 일치하면 session에 정보를 저장
        session.setAttribute("member", member);
        return "redirect:/";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "/account/join";
    }

    @PostMapping("/join")
    public String join(BoardMember member, Model model){
     //   boardMemberMapper.joinMember(member);
        int result = boardMemberService.join(member);

        if (result == -1){
            String msg = "해당 ID는 이미 사용중입니다.";
            model.addAttribute("msg", msg);
            return "/account/join";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("member");
        return "redirect:/";
    }

}
