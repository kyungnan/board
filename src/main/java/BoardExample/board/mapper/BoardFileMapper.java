package BoardExample.board.mapper;

import BoardExample.board.domain.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardFileMapper {

    void uploadFile(File file);

    List<File> getByPostno(int postno);

    void deleteFile(int f_id);
}
