package BoardExample.board.service;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.Reply;

import javax.servlet.http.HttpSession;

public interface BoardService {

    void createPost(String subject, String content, HttpSession session);

    void updatePost(Board post, Board updatePost);

    void createReply(HttpSession session, int postno, String content_reply);
}
