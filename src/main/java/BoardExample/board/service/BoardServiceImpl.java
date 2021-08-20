package BoardExample.board.service;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.domain.Reply;
import BoardExample.board.mapper.BoardMapper;
import BoardExample.board.mapper.BoardReplyMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;
    private final BoardReplyMapper boardReplyMapper;

    @Override
    public void createPost(String subject, String content, HttpSession session) {
        BoardMember member = (BoardMember)session.getAttribute("member");
        Board post = new Board();
        post.setWriter(member.getName());
        post.setRegdate(new Timestamp(System.currentTimeMillis()));
        post.setModidate(new Timestamp(System.currentTimeMillis()));
        post.setSubject(subject);
        post.setContent(content);
        boardMapper.createPost(post);
    }

    @Override
    public void updatePost(Board post, Board updatePost) {
        post.setContent(updatePost.getContent());
        post.setSubject(updatePost.getSubject());
        boardMapper.updatePost(updatePost);
    }

    @Override
    public void createReply(HttpSession session, int postno, String content_reply) {
        Reply reply = new Reply();
        reply.setPostno(postno);
        reply.setReg_date(new Timestamp(System.currentTimeMillis()));
        reply.setId_member(((BoardMember)session.getAttribute("member")).getId());
        reply.setContent_reply(content_reply);
        boardReplyMapper.createReply(reply);
    }
}
