package BoardExample.board.controller;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.domain.Criteria;
import BoardExample.board.domain.PageMaker;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private static Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping("/list")
    public String list(@ModelAttribute("criteria") Criteria criteria, Model model){
        // 리스트 조회
        List<Board> boardList = boardMapper.getListWithPaging(criteria);
        model.addAttribute("boardList", boardList);

        // 게시판 목록 하단 페이징 처리 기능
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        pageMaker.setTotalCnt(boardMapper.getTotalCnt());
        model.addAttribute("pageMaker", pageMaker);
        return "/board/list";
    }

    @GetMapping("/search")
    public String searchList(@RequestParam("searchWriter") String searchWriter, @ModelAttribute("criteria") Criteria criteria, Model model){
        // 리스트 조회
        List<Board> boardList = boardMapper.searchWriter(searchWriter, criteria);
        model.addAttribute("boardList", boardList);

        // 게시판 목록 하단 페이징 처리 기능
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        pageMaker.setTotalCnt(boardMapper.searchCnt(searchWriter));
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("searchWriter", searchWriter);
        return "/board/searchList";
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
    public String content(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage, Model model){
        Board findPost = boardMapper.getByPostNo(postno);
        boardMapper.updateCount(postno);
        model.addAttribute("findPost", findPost);

        Criteria criteria = new Criteria();
        criteria.setPage(page);
        criteria.setCntPerPage(cntPerPage);
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        model.addAttribute("pageMaker", pageMaker);

        return "board/content";
    }

    @GetMapping("/modify/{postno}")
    public String modifyForm(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage, Model model){
        Board findPost = boardMapper.getByPostNo(postno);
        model.addAttribute("findPost",findPost);

        Criteria criteria = new Criteria();
        criteria.setPage(page);
        criteria.setCntPerPage(cntPerPage);
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        model.addAttribute("pageMaker", pageMaker);

        return "board/modify";
    }

    @PostMapping("/modify/{postno}")
    public String modify(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage, Board updatePost){
        Board post = boardMapper.getByPostNo(postno);
        boardService.updatePost(post, updatePost);
        return "redirect:/board/list" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }

    @GetMapping("/remove/{postno}")
    public String modify(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage){
        boardMapper.deletePost(postno);
        return "redirect:/board/list" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }
}
