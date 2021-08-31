package BoardExample.board.service;

import BoardExample.board.config.FileUploadProperties;
import BoardExample.board.domain.Board;
import BoardExample.board.domain.File;
import BoardExample.board.exception.FileUploadException;
import BoardExample.board.mapper.BoardFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardFielServiceImpl implements BoardFileService{

    private final Path fileLocation;
    private final BoardFileMapper boardFileMapper;

    @Autowired
    public BoardFielServiceImpl(FileUploadProperties properties, BoardFileMapper boardFileMapper) {
        this.fileLocation = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        this.boardFileMapper = boardFileMapper;

        try {
            Files.createDirectories(this.fileLocation);
        } catch (IOException e) {
            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.",e);
        }
    }

    @Override
    public File uploadFile(Board post, MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path targetLocation = this.fileLocation.resolve(fileName);
        try {
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileUploadException("[" + fileName +"] 파일 업로드에 실패했습니다.");
        }

        String filePath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        File file = new File();
        file.setF_postno(post.getPostno());
        file.setFile_path(filePath);
        file.setFile_origin_name(fileName);
        file.setFile_name(UUID.randomUUID().toString()+"_"+fileName);
        file.setReg_date(new Timestamp(System.currentTimeMillis()));
        file.setFile_type(multipartFile.getContentType());
        file.setFile_size(multipartFile.getSize());
        boardFileMapper.uploadFile(file);

        return file;
    }

    @Override
    public List<File> uploadFiles(Board post, MultipartFile[] multipartFiles) {
        return Arrays.asList(multipartFiles)
                .stream()
                .map(file -> uploadFile(post, file))
                .collect(Collectors.toList());
    }


}
