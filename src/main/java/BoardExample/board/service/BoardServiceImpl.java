package BoardExample.board.service;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.BoardMember;
import BoardExample.board.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@Service
public class BoardServiceImpl implements BoardService{

    private BoardMapper boardMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

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
}
