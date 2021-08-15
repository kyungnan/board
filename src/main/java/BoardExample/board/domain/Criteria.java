package BoardExample.board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Criteria {
    private int page;           // 보여줄 페이지 번호
    private int cntPerPage;     // 페이지 당 보여줄 게시글 수
    private int startIndex;     // 쿼리에서 사용할 페이지 당 시작 번호

    public Criteria() {
        // 최초 게시판 진입 시 기본 값 초기화
        this.page = 1;
        this.cntPerPage = 10;
    }

    public void setPage(int page){
        if (page < 0){
            this.page = 1;
            return;
        }

        this.page = page;
    }

    public void setCntPerPage(int cntPerPage){
        if (cntPerPage <= 0 || cntPerPage > 100){
            this.cntPerPage = 10;
            return;
        }
        this.cntPerPage = cntPerPage;
    }

    private int getStartIndex(){
        return (this.page - 1) * cntPerPage;
    }
}