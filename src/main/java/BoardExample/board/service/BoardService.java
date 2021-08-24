package BoardExample.board.service;

import BoardExample.board.domain.Board;
import org.springframework.security.core.Authentication;

public interface BoardService {

    void createPost(String subject, String content, Authentication authentication);

    void updatePost(Board post, Board updatePost);

    void createReply(Authentication authentication, int postno, String content_reply);
}
