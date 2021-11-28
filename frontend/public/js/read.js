// 좋아요 버튼 
$(".like-content .btn-secondary").click(function() {
        
  let postId = sessionStorage.getItem("postId");
  let token = sessionStorage.getItem("token"); 
  if(token != null){
    $(this).toggleClass("done");
  }else{
    alert("로그인이 필요합니다.");
    window.location.href = '/view/member/login.html';
  }

  $.ajax({
    url:"http://www.palette-travel.com/api/post/"+postId+"/like",
    method:"POST",
    data: "",
    success:function(data){
      //console.log(data);
      $('#result').text(data.data);
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
    url:"http://www.palette-travel.com/api/post/"+postId+"/like",
    type:"GET",
    contentType: 'application/json; charset=UTF-8',
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml.
    data: "",
    success:function(data){
      
      for(i=0; i<data.likeResponses.length; i++){

        if(memberId == data.likeResponses[i].memberId){                  
          $('.like-content .btn-secondary').toggleClass('done');
        }
        
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

  if(content != ""){
    $.ajax({
      url:"http://www.palette-travel.com/api/post/"+postId+"/comment",
      method:"POST",
      data:JSON.stringify({content:content}),
      dataType:'json',
      contentType: "application/json", 
      success:function(data){
        
        window.location.href = '/view/Board/read.html';
 
        alert('등록되었습니다.');
      },
      error:function(e){
        alert("로그인이 필요합니다.");
        window.location.href = '/view/member/login.html';
        
      },
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);
      }
    });
  }else{
    alert("입력정보를 확인하세요.");
  }
 
}

// ajax 요청 (댓글 수정)
function modifyComment() {

  var content = $('#new-comment2').val();
  let token = sessionStorage.getItem("token");
  let postId = sessionStorage.getItem("postId");
  let commentId = sessionStorage.getItem("commentId");

  $.ajax({
    url:"http://www.palette-travel.com/api/post/"+postId+"/comment/"+commentId,
    method:"PUT",
    data:JSON.stringify({content:content}),
    contentType: "application/json", 
    success:function(data){
     
      window.location.href = '/view/Board/read.html';
      alert('수정되었습니다.');
    },
    error:function(e){
     
    },
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);
    }
  });
}  

function OpenmodifyComment(commentId){
   
  sessionStorage.setItem("commentId", commentId);

  let token = sessionStorage.getItem("token"); 
  let postId = sessionStorage.getItem("postId");
  let memberId = sessionStorage.getItem("memberId");

  var comment = $('#new-comment2').val();

  $.ajax({
    url: 'http://www.palette-travel.com/api/post/'+postId+'/comment?id='+(commentId-1), // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: '',      // 데이터
  
    success: function(Data) { // 서버로부터 성공적으로 응답이 온경우

    for(i=0; i<Data.commentResponses.length; i++){

      if(commentId == Data.commentResponses[i].commentId){
       
        var data = Data.commentResponses[i];
      }
    }
    console.log(data);

    if(data.memberId == memberId){
      document.getElementById("commentModify").style.display="block";
        $('#new-comment2').val(data.commentContent);
    }else{
      alert("수정 권한이 없습니다.");
    }

    var div = $('#commentModify');
    div.css("position", "absolute");
    div.css("top", Math.max(0, (($(window).height() - div.outerHeight()) / 2) + $(window).scrollTop()) + "px");
    div.css("left", Math.max(0, (($(window).width() - div.outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    

    },
  
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      
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

  var confirm_val = confirm("삭제하시겠습니까?");
  if (confirm_val == true) {
    $.ajax({
      url: 'http://www.palette-travel.com/api/post/'+postId+'/comment/'+commentId, // 개발시 변경 부분  
      type: 'DELETE',  // get, post
      success: function(testData) { // 서버로부터 성공적으로 응답이 온경우         
        alert("댓글이 삭제되었습니다.");
        window.location.href = '/view/Board/read.html';
      }, 
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        
        alert("삭제 권한이 없습니다.");
      },
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);
      }
    }); 
  }
  else{
    alert("취소하셨습니다.");
    window.location.href = '/view/Board/read.html';
  }
}

// 수정 페이지
function modifyPost(){
  let token = sessionStorage.getItem("token"); 
  let postId = sessionStorage.getItem("postId");
  let memberId = sessionStorage.getItem("memberId");

  $.ajax({
      url: 'http://www.palette-travel.com/api/post/'+postId, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      //data: '',      // 데이터

      success: function(testData) { // 서버로부터 성공적으로 응답이 온경우

        //console.log(testData);
        if(testData.memberId == memberId){
          window.location.href = "./update.html";
        } else{
          alert("수정 권한이 없습니다.");
        }
        },

      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
       
      },

      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);
     }
  });
}  

//ajax 요청(글목록)
function view() {
  var frm = $('#frmpost');
  let postId = sessionStorage.getItem("postId");
  let memberId = sessionStorage.getItem("memberId");
  var params = '';  // 개발시 변경 부분
  var msg = '';
  
  $.ajax({
    url: 'http://www.palette-travel.com/api/post/'+postId, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터
    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
            
      if (testData.postId > 0) { 
  
  
            $('#title').empty();       
            $('#author').empty();
            $('#photo').empty();       
            $('#content').empty();
            $('#result').empty();
            $('.comment-date').empty();
            $('.comment-content').empty();
  
            $("#title").append("<div>"+testData.postTitle+"</div>");
            $("#author").append("<div>"+testData.memberName+"</div>");
            $("#photo").append("<div><figure class='image'><img src=' " + testData.images[0] + " '/></figure></div>");
            $("#content").append("<div style='white-space:pre;'>"+testData.postContent+"</div>");
            $("#result").append("<div>"+testData.likes+"</div>");
            
            for(i=0; i<testData.comments.length; i++){
             $('.comment-row0').append("<div class = 'comment-row'><div class = 'comment-memberName'>"+testData.comments[i].memberName+"\
                                        </div><div class = 'comment-date'>"+testData.comments[i].createDate.substring(0,10)+"</div><div class = 'comment-content'>\
                                        "+testData.comments[i].commentContent+"</div>\
                                         <input type = 'button' class='modifycomment'  value='수정' onclick='OpenmodifyComment(this.id)' id="+testData.comments[i].commentId +">\
                                         <input type = 'button' class='deletecomment'  value='삭제' onclick='deleteComment(this.id)' id="+testData.comments[i].commentId +"></div>"); 
            }
  
      } else {  
            testData.postGroupResponses[0].postGroupId.value();
            alert('내용 xx' + testData.postGroupResponses[0].postGroupId.val());
        }
    },
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
    
      }
  });
}

//ajax 요청 (글삭제)
//debugger;     
function deletePost() {
  let token = sessionStorage.getItem("token"); 
  let postGroupId = sessionStorage.getItem("postGroupId");
  let postId = sessionStorage.getItem("postId");

  var confirm_val = confirm("삭제하시겠습니까?");
  if (confirm_val == true) {
    $.ajax({
      url: 'http://www.palette-travel.com/api/postgroup/'+ postGroupId+'/post/'+postId, // 개발시 변경 부분  
      type: 'DELETE',  // get, post  
      success: function(testData) { // 서버로부터 성공적으로 응답이 온경우        
          alert("게시글이 삭제되었습니다.");
          window.location.href = "/view/Board/myblogDetail.html"        
      },
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수

        alert("삭제 권한이 없습니다.");
      },
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);
      }
    });
  }else{
    alert("취소하셨습니다.");
    window.location.href = "/view/Board/read.html"
  }
}

// 댓글 더보기 ??
function commentView() {
  var frm = $('#frmpost');
  let postId = sessionStorage.getItem("postId");
  let memberId = sessionStorage.getItem("memberId");
  var params = '';  // 개발시 변경 부분
  var msg = '';
  
  var commentid = 0;
 
  $.ajax({
    url: 'http://www.palette-travel.com/api/post/'+postId+'/comment?id='+commentid, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터
    success: function(Data) { // 서버로부터 성공적으로 응답이 온경우


      for(j=0; j<Data.commentResponses.length; j++){
        var commentid = Data.commentResponses[j].commentId;
     
      }
      //console.log(commentid);
      sessionStorage.setItem("commentid", commentid);
    },
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
       
      }
  });
}

function commentView01() {
  var frm = $('#frmpost');
  let postId = sessionStorage.getItem("postId");
  let memberId = sessionStorage.getItem("memberId");
  let commentid = sessionStorage.getItem("commentid");
  var params = '';  // 개발시 변경 부분
  var msg = '';
  
 
  $.ajax({
    url: 'http://www.palette-travel.com/api/post/'+postId+'/comment?id='+commentid, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터
    success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
    

      for(j=0; j<Data.commentResponses.length; j++){
        var commentid = Data.commentResponses[j].commentId;
   
      }
    
      sessionStorage.setItem("commentid", commentid);
      for(i=0; i<Data.commentResponses.length; i++){
      $('.comment-row0').append("<div class = 'comment-row'><div class = 'comment-memberName'>"+Data.commentResponses[i].memberName+"\
                                        </div><div class = 'comment-date'>"+Data.commentResponses[i].createDate.substring(0,10)+"</div><div class = 'comment-content'>\
                                        "+Data.commentResponses[i].commentContent+"</div>\
                                         <input type = 'button' class='modifycomment'  value='수정' onclick='OpenmodifyComment(this.id)' id="+Data.commentResponses[i].commentId +">\
                                         <input type = 'button' class='deletecomment'  value='삭제' onclick='deleteComment(this.id)' id="+Data.commentResponses[i].commentId +"></div>"); 
      }
    },
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        alert('마지막 댓글입니다.');
      }
  });
}
