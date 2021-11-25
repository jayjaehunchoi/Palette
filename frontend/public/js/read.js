// 좋아요 버튼 
    $(".like-content .btn-secondary").click(function() {
        //$(this).toggleClass("done");
     let postId = sessionStorage.getItem("postId");
     let token = sessionStorage.getItem("token"); 
        if(token != null){
            $(this).toggleClass("done");
        }else{
            alert("로그인이 필요합니다");
        }

        $.ajax({
            url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/like",
            method:"POST",
            data: "",
            success:function(data){
                console.log(data);
                $('#result').text(data.data);
                // like_btn();
            },

beforeSend: function (xhr) {
  xhr.setRequestHeader("Authorization","Bearer " + token);
}
        });
    });

 function like_btn(){
    let postId = sessionStorage.getItem("postId");
    let token = sessionStorage.getItem("token"); 
    let memberId = sessionStorage.getItem("memberId");


    $.ajax({
        url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/like",
        type:"GET",
        contentType: 'application/json; charset=UTF-8',
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml.
        data: "",
        success:function(data){
            console.log(data);
            for(i=0; i<data.likeResponses.length; i++){
                // console.log(memberId);
                // console.log(data[i].memberId);
                if(memberId == data.likeResponses[i].memberId){
                   //빨간하트만들어줘
                      $('.like-content .btn-secondary').toggleClass('done');
                    // $('.like-content').append("<button class = 'btn-secondary like-review done'><i class = 'far fa-heart' aria-hidden = 'true'></i><span class='material-icons'\
                    //  > favorite_border </span></button>")
                }
            console.log(true);
            }

        },
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Authorization","Bearer " + token);
        }
    });
  }

    // 댓글작성
    function submitComment(){

        var content = $('#new-comment').val();
        let token = sessionStorage.getItem("token");
        let postId = sessionStorage.getItem("postId");

        $.ajax({
            url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/comment",
            method:"POST",
            data:JSON.stringify({content:content}),
            dataType:'json',
            contentType: "application/json", 
            success:function(data){
                console.log(data);
                window.location.href = '/view/Board/read.html';
                alert('등록되었습니다');
            },
            error:function(e){
                window.location.href = '/view/Board/read.html';
                 console.log("error : ", e);
                 },

                 beforeSend: function (xhr) {
                   xhr.setRequestHeader("Authorization","Bearer " + token);
                 }
        });

    }

// ajax 요청 (댓글 수정)

function modifyComment() {

    var content = $('#new-comment2').val();
    let token = sessionStorage.getItem("token");
    let postId = sessionStorage.getItem("postId");
    let commentId = sessionStorage.getItem("commentId");

    $.ajax({
        url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/comment/"+commentId,
        method:"PUT",
        data:JSON.stringify({content:content}),
        //dataType:'json',
        contentType: "application/json", 
        success:function(data){
            console.log(data);
            window.location.href = '/view/Board/read.html';
            alert('수정되었습니다');
        },
        error:function(e){
             console.log("error : ", e);
             },

             beforeSend: function (xhr) {
               xhr.setRequestHeader("Authorization","Bearer " + token);
             }
    });
}  

function OpenmodifyComment(commentId){
   // document.getElementById("commentModify").style.display="block";
    sessionStorage.setItem("commentId", commentId);

    let token = sessionStorage.getItem("token"); 
    let postId = sessionStorage.getItem("postId");
    let memberId = sessionStorage.getItem("memberId");

    var comment = $('#new-comment2').val();

    $.ajax({
        url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/'+postId+'/comment', // 개발시 변경 부분
        contentType: 'application/json; charset=UTF-8',
        type: 'get',  // get, post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: '',      // 데이터
  
        success: function(Data) { // 서버로부터 성공적으로 응답이 온경우

            for(i=0; i<Data.commentResponses.length; i++){

                if(commentId == Data.commentResponses[i].commentId){
                  console.log(Data.commentResponses[i]);
                  var data = Data.commentResponses[i];
                }
              }

              console.log(data);

              if(data.memberId == memberId){
                document.getElementById("commentModify").style.display="block";
                $('#new-comment2').val(data.commentContent);
              }else{
                alert("수정 권한이 없습니다");
              }
              //$('#new-comment2').val(data.commentContent);


          },
  
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          alert('ajax야 힘내자'+ request +status + error);
        },

        beforeSend: function (xhr) {
          xhr.setRequestHeader("Authorization","Bearer " + token);
       }
      });
}

function closemodifyComment() {
    document.getElementById("commentModify").style.display="none";
  }   

// ajax 요청 (댓글 삭제)
function deleteComment(commentId) {
    let token = sessionStorage.getItem("token"); 
    let postGroupId = sessionStorage.getItem("postGroupId");
    let postId = sessionStorage.getItem("postId");
    sessionStorage.setItem("commentId", commentId);

    $.ajax({
        url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/'+postId+'/comment/'+commentId, // 개발시 변경 부분  
        type: 'DELETE',  // get, post
    
        success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
        
        if (confirm("삭제하시겠습니까?")) {
            alert("게시글이 삭제되었습니다.");
            window.location.href = '/view/Board/read.html';
        }
        }, 
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          //alert('ajax야 힘내자'+ request +status + error);
          console.log('ajax야 힘내자'+ request +status + error);
          alert("삭제 권한이 없습니다");
        },
  
  beforeSend: function (xhr) {
    xhr.setRequestHeader("Authorization","Bearer " + token);
  }
      });
    }

    
// if(memberId=Id){
//     document.getElementById("modify").style.display="block";
// }else{
//     document.getElementById("modify").style.display="none";
// }

