package BoardExample.board.controller;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.*;
import BoardExample.board.mapper.*;
import BoardExample.board.service.BoardFileService;
import BoardExample.board.service.BoardLikeService;
import BoardExample.board.service.BoardService;
import BoardExample.board.utils.Criteria;
import BoardExample.board.utils.PageMaker;
import BoardExample.board.vo.BoardDetailsVo;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final BoardLikeService boardLikeService;
    private final BoardLikeMapper boardLikeMapper;

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
    public String content(@PathVariable int postno, BoardDetailsVo boardDetailsVo, Criteria criteria,
                          HttpServletRequest request, HttpServletResponse response,
                          Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        //model 속성
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

        BoardMember sessionMember = boardMemberMapper.getById(principalDetails.getUsername());
        model.addAttribute("sessionMember", sessionMember);

        Like like = boardLikeMapper.getByPostnoAndMember(postno, sessionMember.getId());
        model.addAttribute("like", like);

        Integer likeCount = boardLikeMapper.LikeCountGetByPostno(postno);
        model.addAttribute("likeCount", likeCount);

        Board findPost = boardMapper.getByPostNo(postno);
        model.addAttribute("findPost", findPost);

        List<Reply> replyList = boardService.getReply(postno);
        model.addAttribute("replyList", replyList);

        //첨부파일 목록
        List<File> fileList = boardFileMapper.getByPostno(postno);
        for (File file:fileList){
            log.debug(file.getFile_origin_name());
        };
        model.addAttribute("fileList", fileList);

        //조회수 중복방지
        Cookie[] cookies = request.getCookies();
        int visitor = 0;
        String cookiePostNo = String.valueOf(postno);

        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName());

            if (cookie.getName().equals("visit")) {
                visitor = 1;

                System.out.println("visit 통과");
                if (cookie.getValue().contains(cookiePostNo)) {
                    System.out.println("viditIf 통과");
                } else {
                    cookie.setValue(cookie.getValue() + "_" + postno);
                    response.addCookie(cookie);
                    boardMapper.updateCount(postno);
                }
            }
        }

        if (visitor == 0) {
            Cookie newCookie = new Cookie("visit", cookiePostNo);
            response.addCookie(newCookie);

            boardMapper.updateCount(postno);
        }

        return "board/content";
    }

    // 검색 게시글 조회
    @GetMapping("/search")
    public String searchList(@RequestParam("searchWriter") String searchWriter,
                             @ModelAttribute("criteria") Criteria criteria, Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails){
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
                log.debug("단일 파일 첨부");
                boardFileService.uploadFile(insertPost, multipartFile);
            }
            else {
                log.debug("다중 파일 첨부");
                boardFileService.uploadFiles(insertPost, multipartFiles);
            }
        }

        return "redirect:/board";       //redirect:/ 없이 board/list 하면 글 쓰기 후 리스트 보여줄때 제대로 반영 X
    }

    // 글 수정 폼
    @GetMapping("/post/{postno}")
    public String modifyForm(@PathVariable int postno, Criteria criteria, Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        model.addAttribute("nameUserDetails", nameUserDetails);

        List<File> fileList = boardFileMapper.getByPostno(postno);
        model.addAttribute("fileList", fileList);

        Board findPost = boardMapper.getByPostNo(postno);
        model.addAttribute("findPost",findPost);

//        Criteria criteria = new Criteria();
//        criteria.setPage(page);
//        criteria.setCntPerPage(cntPerPage);
//        PageMaker pageMaker = new PageMaker();
//        pageMaker.setCriteria(criteria);
//        model.addAttribute("pageMaker", pageMaker);

        return "board/modify";
    }

    // 글 수정 처리
    @PutMapping("/{postno}")
    public String modify(@PathVariable("postno") int postno, Criteria criteria,
                         @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam(value = "file", required = false) MultipartFile[] multipartFiles,
                         Board updatePost){
        Board post = boardMapper.getByPostNo(postno);
        boardService.updatePost(post, updatePost);

        if (!multipartFile.isEmpty()){

            if (multipartFiles.length == 1){
                log.debug("단일 파일 첨부");
                boardFileService.uploadFile(post, multipartFile);
            }
            else {
                log.debug("다중 파일 첨부");
                boardFileService.uploadFiles(post, multipartFiles);
            }
        }
        return "redirect:/board" + "?page=" + criteria.getPage() + "&cntPerPage=" + criteria.getCntPerPage();
    }

    // 글 삭제 처리
    @DeleteMapping("/{postno}")
    public String delete(@PathVariable("postno") int postno, Criteria criteria){
        boardMapper.deletePost(postno);
        return "redirect:/board" + "?page=" + criteria.getPage() + "&cntPerPage=" + criteria.getCntPerPage();
    }

    // 댓글 입력 처리
    @PostMapping("/{postno}/reply")
    public ResponseEntity<String> replySave(@PathVariable int postno, @RequestBody String content_reply, @AuthenticationPrincipal PrincipalDetails principalDetails){
        boardService.createReply(principalDetails, postno, content_reply);
        log.debug("댓글 등록 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 삭제 처리
    @DeleteMapping("/{postno}/reply/{id_reply}")
    public ResponseEntity<String> replyDelete(@PathVariable("id_reply") int id_reply){
        boardReplyMapper.deleteReply(id_reply);
        log.debug("댓글 삭제 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 댓글 수정 폼
    @GetMapping("/{postno}/reply/{id_reply}")
    public String replyUpdateForm(@PathVariable("postno") int postno, @PathVariable("id_reply") int id_reply,
                                  Criteria criteria){
        log.debug("수정버튼 클릭하여 댓글 수정 폼 요청");
        log.debug("id_reply" + id_reply);
        return "redirect:/board/{postno}" + "?updateIdReply=" + id_reply + "&flag=true"
                + "&page=" + criteria.getPage() + "&cntPerPage=" + criteria.getCntPerPage();
    }

    // 댓글 수정 처리
    @PutMapping("/{postno}/reply/{updateIdReply}")
    public ResponseEntity<String> replyUpdate(@PathVariable("postno") int postno, @PathVariable("updateIdReply") int updateIdReply,@RequestBody String content_reply,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        log.debug("수정댓글내용 > " + content_reply);
        log.debug("수정댓글번호 > " + updateIdReply);
        boardService.updateReply(updateIdReply, postno, content_reply);
        log.debug("댓글 수정 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 대댓글 입력 폼
    @GetMapping("/{postno}/{id_reply}")
    public String replyInsertForm(@PathVariable("postno") int postno, @PathVariable("id_reply") int parent_id,
                                  Criteria criteria){
        log.debug("답글버튼 클릭하여 답글 입력 폼 요청");
        log.debug("parent_id : " + parent_id);
        return "redirect:/board/{postno}" + "?parent_id=" + parent_id + "&flagReReply=true"
                + "&page=" + criteria.getPage() + "&cntPerPage=" + criteria.getCntPerPage();
    }

    // 대댓글 처리
    @PostMapping("/{postno}/{parent_id}")
    public ResponseEntity<String> re_replyCreate(@PathVariable("postno") int postno, @PathVariable("parent_id") int parent_id,@RequestBody String content_reply,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){
        boardService.createReReply(principalDetails, postno, parent_id, content_reply);
        log.debug("대댓글 등록 완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 첨부파일 삭제 처리
    @DeleteMapping("/{postno}/attach/{f_id}")
    public ResponseEntity<String> attachDelete(@PathVariable("f_id") int f_id){
        boardFileMapper.deleteFile(f_id);
        log.debug("첨부파일 삭제 완료");
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
            log.debug("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    //좋아요 체크
    @GetMapping("/{like_postno}/like/{like_member}")
    public ResponseEntity<String> addLike(@PathVariable("like_postno") int like_postno, @PathVariable("like_member") int like_member){
        log.debug(like_postno + " // " + like_member);
        boardLikeService.checkLike(like_postno, like_member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}