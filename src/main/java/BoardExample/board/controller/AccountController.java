package BoardExample.board.controller;

import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.service.BoardMemberService;
import BoardExample.board.utils.ScriptUtils;
import BoardExample.board.validation.JoinValidation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@ControllerAdvice
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
    public String join(@Valid BoardMember member, Errors errors, Model model){

        String checkResult = JoinValidation.joinValicationCheck(errors, model);
        if (checkResult != null) return checkResult;

        int result = boardMemberService.join(member);
        if (result == -1){
            String msg = "해당 ID는 이미 사용중입니다.";
            model.addAttribute("msg", msg);
            return "members/join";
        }
        return "redirect:/members/login";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable int id, Model model){
        BoardMember member = boardMemberMapper.getById(id);
        model.addAttribute("member", member);
        return "members/info";
    }

    @PutMapping("/{id}")
    public void info(@PathVariable("id") int id, BoardMember updateMember, HttpServletResponse response){
        BoardMember originMember = boardMemberMapper.getById(id);
        boardMemberService.update(originMember, updateMember);

        try {
            ScriptUtils.alertAndMovePage(response, "회원정보 수정이 완료되었습니다.", "/members/" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
