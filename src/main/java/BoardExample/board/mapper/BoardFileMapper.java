package BoardExample.board.mapper;

import BoardExample.board.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardFileMapper {

    void uploadFile(File file);
}
