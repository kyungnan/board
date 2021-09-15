package BoardExample.board.service;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.Board;
import BoardExample.board.domain.Reply;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface BoardService {

    void createPost(Board post, @AuthenticationPrincipal PrincipalDetails principalDetails);

    void updatePost(Board post, Board updatePost);

    List<Reply> getReply(int postno);

    void createReply(@AuthenticationPrincipal PrincipalDetails principalDetails, int postno, String content_reply);

    void updateReply(int updateIdReply, int postno, String content_reply);

    void createReReply(@AuthenticationPrincipal PrincipalDetails principalDetails, int postno, int parent_id, String content_reply);
}
