//ajax 요청 (글 목록)

function view() {
    var frm = $('#myblogDetailUpdate');
    let token = sessionStorage.getItem("token"); 
    let postId = sessionStorage.getItem("postId");
    var params = '';  // 개발시 변경 부분
    var msg = '';

    $.ajax({
      url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/'+postId, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터

      success: function(testData) { // 서버로부터 성공적으로 응답이 온경우

           //div 초기화
           $('#photo').empty();

          console.log(testData);
          $("#title").val(testData.postTitle);
          $('.image img').attr("src",testData.images[0]);
          $("#content").val(testData.postContent);
          console.log(testData);
          console.log(testData.images[0]);
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

  //ajax 요청 (글수정)
  function modify() {
    var frm = $('#myblogDetailUpdate')[0];
    var title = $('#title',frm).val();
    var content = $('#content ',frm).val();
   
    let token = sessionStorage.getItem("token"); 
    let postGroupId = sessionStorage.getItem("postGroupId");
    let postId = sessionStorage.getItem("postId");

    var params = { title : title , content : content };  // 개발시 변경 부분
   
    var formData = new FormData(frm);
      console.log(params);
      //console.log(thumbNailFullPath[0].files[0]);
     // formData.append('files',thumbNailFullPath[0].files[0]);
      formData.append('data',new Blob([JSON.stringify(params)] , {type: "application/json"}))   
   
      $.ajax({
         url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/'+postGroupId+'/post/'+postId, // 개발시 변경 부분
         //contentType: false,
         contentType: 'application/json',
         //processData:false,
         type: 'PUT',  // get, post
         cache: false, // 응답 결과 임시 저장 취소
         async: true,  // true: 비동기 통신
         //dataType: 'json', // 응답 형식: json, html, xml...
         data: JSON.stringify(params),      // 데이터
   
         success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
          alert("수정이 완료되었습니다");  
          window.location.href = '/view/Board/read.html'; 
           if (testData != null) { 
   
             console.log(testData);
   
           } else {  
            // alert('내용 xx' + testData.postGroupResponses[0].postGroupId.val());
           }
        
          //  let postGroupId = JSON.parse(testData).postGroupId;

          //  if (window.sessionStorage) { 
          //    sessionStorage.setItem('postGroupId', postGroupId);
          //  }
        },
   
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          //alert('ajax야 힘내자'+ request +status + error);
          console.log('ajax야 힘내자'+ request +status + error);
         },

        beforeSend: function (xhr) {
          xhr.setRequestHeader("Authorization","Bearer " + token);
       }
      });
  }