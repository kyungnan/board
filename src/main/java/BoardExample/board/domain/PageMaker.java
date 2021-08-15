package BoardExample.board.domain;

import lombok.Data;

@Data
public class PageMaker {
    private int totalCnt;
    private int displayPageNum = 5;

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private Criteria criteria;

    public void setTotalCnt(int totalCnt){
        this.totalCnt = totalCnt;
        calcPage();
    }

    public void calcPage(){
        endPage = (int)(Math.ceil(criteria.getPage() / (double) displayPageNum) * displayPageNum);

        startPage = (endPage - displayPageNum) + 1;

        int tempEndPage = (int) (Math.ceil(totalCnt / (double) criteria.getCntPerPage()));

        if (endPage > tempEndPage){
            endPage = tempEndPage;
        }

        prev = startPage == 1 ? false : true;
        next = endPage * criteria.getCntPerPage() >= totalCnt ? false : true;
    }
}
