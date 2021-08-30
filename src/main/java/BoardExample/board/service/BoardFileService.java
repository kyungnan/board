package BoardExample.board.service;

import BoardExample.board.domain.Board;
import org.springframework.web.multipart.MultipartFile;

public interface BoardFileService {
    void uploadFile(Board post, MultipartFile multipartFile);
}
