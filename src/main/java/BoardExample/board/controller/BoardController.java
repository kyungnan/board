package BoardExample.board.controller;

import BoardExample.board.domain.*;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.mapper.BoardReplyMapper;
import BoardExample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private static Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private final BoardReplyMapper boardReplyMapper;

    // 게시글 전체 목록
    @GetMapping
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

    // 게시글 상세보기
    @GetMapping("/{postno}")
    public String content(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage, Model model){
        Board findPost = boardMapper.getByPostNo(postno);
        boardMapper.updateCount(postno);
        model.addAttribute("findPost", findPost);

        List<Reply> replyList = boardReplyMapper.getByPostNo(postno);
        model.addAttribute("replyList", replyList);

        Criteria criteria = new Criteria();
        criteria.setPage(page);
        criteria.setCntPerPage(cntPerPage);
        PageMaker pageMaker = new PageMaker();
        pageMaker.setCriteria(criteria);
        model.addAttribute("pageMaker", pageMaker);
        return "board/content";
    }
    
    // 검색 게시글 조회
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

    // 글 입력 폼
    @GetMapping("/post")
    public String postForm(){
        return "/board/post";
    }

    // 글 입력 처리
    @PostMapping("/post")
    public String post(String subject, String content, HttpSession session){
        boardService.createPost(subject, content, session);
        return "redirect:/board";       //redirect:/ 없이 board/list 하면 글 쓰기 후 리스트 보여줄때 제대로 반영 X
    }

    // 글 수정 폼
    @GetMapping("/post/{postno}")
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

    // 글 수정 처리
    @PutMapping("/{postno}")
    public String modify(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage, Board updatePost){
        Board post = boardMapper.getByPostNo(postno);
        boardService.updatePost(post, updatePost);
        return "redirect:/board" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }

    // 글 삭제 처리
    @DeleteMapping("/{postno}")
    public String modify(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage){
        boardMapper.deletePost(postno);
        return "redirect:/board" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }
}
