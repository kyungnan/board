package BoardExample.board.service;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.Board;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.domain.Reply;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.mapper.BoardMemberMapper;
import BoardExample.board.mapper.BoardReplyMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;
    private final BoardReplyMapper boardReplyMapper;
    private final BoardMemberMapper boardMemberMapper;
    private final BoardFileService boardFileService;

    @Override
    public void createPost(Board post, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());

        post.setWriter(nameUserDetails);
        post.setRegdate(new Timestamp(System.currentTimeMillis()));
        post.setModidate(new Timestamp(System.currentTimeMillis()));

        boardMapper.createPost(post);
    }

    @Override
    public void updatePost(Board post, Board updatePost) {
        post.setContent(updatePost.getContent());
        post.setSubject(updatePost.getSubject());
        boardMapper.updatePost(updatePost);
    }

    @Override
    public void createReply(@AuthenticationPrincipal PrincipalDetails principalDetails, int postno, String content_reply) {
        String nameUserDetails = boardMemberMapper.getNameById(principalDetails.getUsername());
        BoardMember member = boardMemberMapper.getById(principalDetails.getUsername());

        Reply reply = new Reply();
        reply.setPostno(postno);
        reply.setReg_date(new Timestamp(System.currentTimeMillis()));
        reply.setId_member(member.getId());
        reply.setContent_reply(content_reply);
        boardReplyMapper.createReply(reply);
    }

    @Override
    public void updateReply(int id_reply, int postno, String content_reply) {
        Reply reply = boardReplyMapper.getById_reply(id_reply);
        reply.setContent_reply(content_reply);
        boardReplyMapper.updateReply(reply);
    }
}
