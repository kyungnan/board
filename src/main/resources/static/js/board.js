<!-- 댓글 작성 이벤트 -->
$(function() {
    $("#btn_reply_save").click(function(){
        if ($("#content_reply").val() =="" || $("#content_reply").val() == null){
            alert("내용을 입력해주세요.");
            $("#content_reply").focus();
            return false;
        }
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
    var del_confiem = confirm("삭제하시겠습니까?");

    if (del_confiem == true){
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
}

<!-- 댓글 수정 이벤트 -->
$(function() {
    $("#btn_reply_update").click(function(){
        return replyUpdate();
    });

    let postno = $("#postno_update").val();
    let page = $("#page_update").val();
    let cntPerPage = $("#cntPerPage_update").val();
    let id_reply = $("#id_reply_update").val();

    function replyUpdate(){
        let data = {
            content:$("#content_reply_update").val()
        };

        $.ajax({
            type:"PUT",
            url:`/board/${postno}/reply/${id_reply}`,
            data:data.content,
            contentType: "application/json; charset=utf-8;",
            dataType:"text"
        }).done(function(resp){
        alert("댓글 수정이 완료되었습니다.");
        location.href=`/board/${postno}?page=${page}&cntPerPage=${cntPerPage}`;
        }).fail(function(error){
        alert(JSON.stringify(error));
        });
    }
});

<!-- 답글 입력 이벤트 -->
$(function() {
    $("#btn_reply_rereply").click(function(){
        return reReplySave();
    });

    let postno = $("#postno_rereply").val();
    let page = $("#page_rereply").val();
    let cntPerPage = $("#cntPerPage_rereply").val();
    let id_reply = $("#id_reply_rereply").val();

    function reReplySave(){
        let data = {
            content:$("#content_reply_rereply").val()
        };

        $.ajax({
            type:"POST",
            url:`/board/${postno}/${id_reply}`,
            data:data.content,
            contentType: "application/json; charset=utf-8;",
            dataType:"text"
        }).done(function(resp){
        alert("답글 입력이 완료되었습니다.");
        location.href=`/board/${postno}?page=${page}&cntPerPage=${cntPerPage}`;
        }).fail(function(error){
        alert(JSON.stringify(error));
        });
    }
});

<!-- 첨부파일 삭제 이벤트 -->
function attachDelete(f_postno, f_id){
    var del_confiem = confirm("삭제하시겠습니까?");
    if (del_confiem == true){
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
}