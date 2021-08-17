package BoardExample.board.controller;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private final BoardMemberMapper boardMemberMapper;

    public AccountController(BoardMemberMapper boardMemberMapper) {
        this.boardMemberMapper = boardMemberMapper;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/account/login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session) {
        BoardMember member = boardMemberMapper.getById(username);
        //id가 DB에 존재하지 않으면,
        if (member == null) {
            return "redirect:/account/login";
        }

        //id는 DB에 존재하지만, password가 일치하지 않으면,
        if (!password.equals(member.getPassword())) {
            return "redirect:/account/login";
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
    public String join(BoardMember member){
        boardMemberMapper.joinMember(member);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("member");
        return "redirect:/";
    }

}
