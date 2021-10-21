package BoardExample.board.controller;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.service.BoardMemberService;
import BoardExample.board.validation.JoinValidation;
import BoardExample.board.validation.ParameterNullCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String info(@PathVariable("id") int id, BoardMember updateMember){
        BoardMember originMember = boardMemberMapper.getById(id);
        boardMemberService.update(originMember, updateMember);
        return "redirect:/members/" + id;
    }

}
