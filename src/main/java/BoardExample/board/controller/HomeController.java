package BoardExample.board.controller;

import BoardExample.board.mapper.BoardMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final BoardMemberMapper boardMemberMapper;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String nameUserDetails = boardMemberMapper.getNameById(userDetails.getUsername());
            model.addAttribute("nameUserDetails", nameUserDetails);
        }
        return "home";
    }
}
