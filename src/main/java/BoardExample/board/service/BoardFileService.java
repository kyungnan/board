package BoardExample.board.service;

import BoardExample.board.domain.Board;
import BoardExample.board.domain.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardFileService {
    File uploadFile(Board post, MultipartFile multipartFile);

    List<File> uploadFiles(Board post, MultipartFile[] multipartFiles);

    Resource downloadFile(String fileName);
}
