package BoardExample.board.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Board {
    private int postno;
    private String subject;
    private String content;
    private String writer;
    private Timestamp regdate;
    private Timestamp modidate;
    private int count;
}
