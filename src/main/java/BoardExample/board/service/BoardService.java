package BoardExample.board.service;

import BoardExample.board.domain.Board;

import javax.servlet.http.HttpSession;

public interface BoardService {

    void createPost(String subject, String content, HttpSession session);
}
