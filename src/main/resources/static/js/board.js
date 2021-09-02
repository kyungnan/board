<!-- 댓글 작성 이벤트 -->
$(function() {
    $("#btn_reply_save").click(function(){
        return replySave();
    });

    let postno = $("#postno").val();
    let page = $("#page").val();
    let cntPerPage = $("#cntPerPage").val();

    function replySave(){
        let data = {
            content:$("#content_reply").val()
        };

        $.ajax({
            type:"POST",
            url:`/board/${postno}/reply`,
            data:data.content,
            contentType: "application/json; charset=utf-8;",
            dataType:"text"
        }).done(function(resp){
        alert("댓글 작성이 완료되었습니다.");
        location.href=`/board/${postno}?page=${page}&cntPerPage=${cntPerPage}`;
        }).fail(function(error){
        alert(JSON.stringify(error));
        });
    }
});

<!-- 댓글 삭제 이벤트 -->
function replyDelete(postno, id_reply){
//    alert('reply delete function');
    let page = $("#select_page").val();
    let cntPerPage = $("#select_cntPerPage").val();
    $.ajax({
        type:"DELETE",
        url:`/board/${postno}/reply/${id_reply}`,
        dataType:"text"
    }).done(function(resp){
        alert("댓글이 삭제되었습니다.");
        location.href=`/board/${postno}?page=${page}&cntPerPage=${cntPerPage}`;
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}
<!-- 첨부파일 삭제 이벤트 -->
function attachDelete(f_postno, f_id){
//    alert('attachDelete btn click!');
    let page = $("#select_page_del").val();
    let cntPerPage = $("#select_cntPerPage_del").val();
    $.ajax({
        type:"DELETE",
        url:`/board/${f_postno}/attach/${f_id}`,
        dataType:"text"
    }).done(function(resp){
        alert("첨부파일이 삭제되었습니다.");
        type:"GET";
        location.href=`/board/${f_postno}?page=${page}&cntPerPage=${cntPerPage}`;
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}