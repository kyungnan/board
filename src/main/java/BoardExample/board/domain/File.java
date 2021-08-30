package BoardExample.board.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class File {
    private int f_id;
    private int f_postno;
    private String file_origin_name;
    private String file_name;
    private String file_path;
    private String file_type;
    private long file_size;
    private Timestamp reg_date;
    private char delete_yn;

    public File(int f_postno, String file_origin_name, String file_name, String file_path, String file_type, long file_size, Timestamp reg_date, char delete_yn) {
        this.f_postno = f_postno;
        this.file_origin_name = file_origin_name;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_type = file_type;
        this.file_size = file_size;
        this.reg_date = reg_date;
        this.delete_yn = delete_yn;
    }
}
