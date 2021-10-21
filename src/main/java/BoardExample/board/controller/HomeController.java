package BoardExample.board.controller;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final BoardMemberMapper boardMemberMapper;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails != null){
            BoardMember member = boardMemberMapper.getByUsername(principalDetails.getUsername());
            model.addAttribute("member", member);
        }
        return "home";
    }
}
