//function loginCheck(){
//	var loginForm = document.loginForm;
//
//	if(!loginForm.username.value){
//	  alert("ID를 입력하세요.");
//	  loginForm.username.focus();
//	  return false;
//	}
//
//	if(!loginForm.password.value){
//	  alert("비밀번호를 입력하세요.");
//	  loginForm.password.focus();
//	  return false;
//	}
//
// };

$(function() {
    $("#btn_reply_save").click(function(){
        return replySave();
    });

    function replySave(){
        let data = {
            content:$("#content_reply").val()
        };
        let postno = $("#postno").val();
        let page = $("#page").val();
        let cntPerPage = $("#cntPerPage").val();

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
