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

function replyDelete(postno, id_reply){
    //alert('reply delete function');
    let page_del = $("#select_page").val();
    let cntPerPage_del = $("#select_cntPerPage").val();
    $.ajax({
        type:"DELETE",
        url:`/board/${postno}/reply/${id_reply}`,
        dataType:"text"
    }).done(function(resp){
        alert("댓글이 삭제되었습니다.");
        location.href=`/board/${postno}?page=${page_del}&cntPerPage=${cntPerPage_del}`;
    }).fail(function(error){
        alert(JSON.stringify(error));
    });
}