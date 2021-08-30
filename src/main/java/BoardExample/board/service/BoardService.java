package BoardExample.board.service;

import BoardExample.board.config.auth.PrincipalDetails;
import BoardExample.board.domain.Board;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    void createPost(Board post, @AuthenticationPrincipal PrincipalDetails principalDetails,
                    MultipartFile multipartFile);

    void updatePost(Board post, Board updatePost);

    void createReply(@AuthenticationPrincipal PrincipalDetails principalDetails, int postno, String content_reply);
}
