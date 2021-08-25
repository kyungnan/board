package BoardExample.board.service;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.Board;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface BoardService {

    void createPost(String subject, String content, @AuthenticationPrincipal PrincipalDetails principalDetails);

    void updatePost(Board post, Board updatePost);

    void createReply(@AuthenticationPrincipal PrincipalDetails principalDetails, int postno, String content_reply);
}
