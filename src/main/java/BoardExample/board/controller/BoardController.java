package BoardExample.board.controller;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.*;
import BoardExample.board.mapper.BoardFileMapper;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.mapper.BoardReplyMapper;
import BoardExample.board.service.BoardFileService;
import BoardExample.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private final BoardReplyMapper boardReplyMapper;
    private final BoardMemberMapper boardMemberMapper;
    private final BoardFileService boardFileService;
    private final BoardFileMapper boardFileMapper;

    // 게시글 전체 목록
    @GetMapping
    public String list(@ModelAttribute("criteria") Criteria criteria, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

        // 총 게시글 수
        int totalCnt = boardMapper.getTotalCnt();
        model.addAttribute("totalCnt", totalCnt);

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
    public String content(@PathVariable int postno, @RequestParam int page, @RequestParam int cntPerPage, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

        List<File> fileList = boardFileMapper.getByPostno(postno);
        for (File file:fileList){
            log.info(file.getFile_origin_name());
        };
        model.addAttribute("fileList", fileList);

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
    public String searchList(@RequestParam("searchWriter") String searchWriter, @ModelAttribute("criteria") Criteria criteria, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

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
    public String postForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);
        return "/board/post";
    }

    // 글 입력 처리
    @PostMapping("/post")
    public String post(Board insertPost, @AuthenticationPrincipal PrincipalDetails principalDetails,
                       @RequestParam(value = "file", required = false)MultipartFile multipartFile,
                       @RequestParam(value = "file", required = false)MultipartFile[] multipartFiles){

        boardService.createPost(insertPost, principalDetails);

        if (!multipartFile.isEmpty()){

            if (multipartFiles.length == 1){
                log.info("단일 파일 첨부");
                boardFileService.uploadFile(insertPost, multipartFile);
            }
            else {
                log.info("다중 파일 첨부");
                boardFileService.uploadFiles(insertPost, multipartFiles);
            }
        }

        return "redirect:/board";       //redirect:/ 없이 board/list 하면 글 쓰기 후 리스트 보여줄때 제대로 반영 X
    }

    // 글 수정 폼
    @GetMapping("/post/{postno}")
    public String modifyForm(@PathVariable int postno, @RequestParam int page, @RequestParam int cntPerPage, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

        List<File> fileList = boardFileMapper.getByPostno(postno);
        model.addAttribute("fileList", fileList);

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
    public String modify(@PathVariable("postno") int postno, @RequestParam("page") int page, @RequestParam("cntPerPage") int cntPerPage,
                         @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam(value = "file", required = false) MultipartFile[] multipartFiles,
                         Board updatePost){
        Board post = boardMapper.getByPostNo(postno);
        boardService.updatePost(post, updatePost);

        if (!multipartFile.isEmpty()){

            if (multipartFiles.length == 1){
                log.info("단일 파일 첨부");
                boardFileService.uploadFile(post, multipartFile);
            }
            else {
                log.info("다중 파일 첨부");
                boardFileService.uploadFiles(post, multipartFiles);
            }
        }
        return "redirect:/board" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }

    // 글 삭제 처리
    @DeleteMapping("/{postno}")
    public String delete(@PathVariable("postno") int postno, @RequestParam int page, @RequestParam int cntPerPage){
        boardMapper.deletePost(postno);
        return "redirect:/board" + "?page=" + page + "&cntPerPage=" + cntPerPage;
    }

    // 댓글 입력 처리
    @PostMapping("/{postno}/reply")
    public ResponseEntity<String> replySave(@PathVariable int postno, @RequestBody String content_reply, @AuthenticationPrincipal PrincipalDetails principalDetails){
        boardService.createReply(principalDetails, postno, content_reply);
        log.info("댓글 등록 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 삭제 처리
    @DeleteMapping("/{postno}/reply/{id_reply}")
    public ResponseEntity<String> replyDelete(@PathVariable("id_reply") int id_reply){
        boardReplyMapper.deleteReply(id_reply);
        log.info("댓글 삭제 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 첨부파일 삭제 처리
    @DeleteMapping("/{postno}/attach/{f_id}")
    public ResponseEntity<String> attachDelete(@PathVariable("f_id") int f_id){
        boardFileMapper.deleteFile(f_id);
        log.info("첨부파일 삭제 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 첨부파일 다운로드
    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = boardFileService.downloadFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
