package BoardExample.board.controller;

import BoardExample.board.domain.Board;
import BoardExample.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String list(){
        return "/board/list";
    }

    @GetMapping("/post")
    public String postForm(){
        return "/board/post";
    }

    @PostMapping("/post")
    public String post(String subject, String content, HttpSession session){
        boardService.createPost(subject, content, session);
        return "/board/list";
    }
}
