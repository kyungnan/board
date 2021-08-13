package BoardExample.board.controller;

import BoardExample.board.domain.Board;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardMapper boardMapper;

    public BoardController(BoardService boardService, BoardMapper boardMapper) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Board> boardList = boardMapper.getList();

        model.addAttribute("boardList", boardList);
        return "/board/list";
    }

    @GetMapping("/post")
    public String postForm(){
        return "/board/post";
    }

    @PostMapping("/post")
    public String post(String subject, String content, HttpSession session){
        boardService.createPost(subject, content, session);
        return "redirect:/board/list";       //redirect:/ 없이 board/list 하면 글 쓰기 후 리스트 보여줄때 제대로 반영 X
    }

    @GetMapping("/content/{postno}")
    public String content(@PathVariable("postno") int postno, Model model){
        Board findPost = boardMapper.getByPostNo(postno);
        model.addAttribute("findPost", findPost);
        return "board/content";
    }

    @GetMapping("/modify/{postno}")
    public String modifyForm(@PathVariable("postno") int postno, Model model){
        Board findPost = boardMapper.getByPostNo(postno);
        model.addAttribute("findPost",findPost);
        return "board/modify";
    }

    @PostMapping("/modify/{postno}")
    public String modify(@PathVariable("postno") int postno, Board updatePost){
        Board post = boardMapper.getByPostNo(postno);
        boardService.updatePost(post, updatePost);
        return "redirect:/board/list";
    }
}
