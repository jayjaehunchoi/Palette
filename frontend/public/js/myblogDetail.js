// ajax 요청 

function view(){
  var frm = $('#frmMyBlogDetail');
  var page = $('#page',frm).val();

  var params = '';
  var msg = '';
  let postGroupId = sessionStorage.getItem("postGroupId");

  $.ajax({
    url: 'http://www.palette-travel.com/api/postgroup/'+ postGroupId, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터

    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
      sessionStorage.removeItem("page");      
      if (testData.storyLists.length > 0) { 
              
      //div 초기화
      $('.card-image').empty();       

        for(i=0; i < testData.storyLists.length; i++ ){
         
          msg += '<div class="inner">'
          msg += '<div onclick="fnMyblogDetail(this.id);" id="' + testData.storyLists[i].postId + '">'
          msg += '<figure class="image">'
          msg += '<img src='  
          msg +=  testData.storyLists[i].thumbNailFullPath 
          msg += ' /></figure></div></div>';
          console.log(testData.storyLists[i].postId);
        }
        
      $('.card-image').html(msg);

      } else {   
          $('.card-image').empty();         
          $('.card-image').append("<div class='material-icons'>error_outline</div><div class = 'notPost'>게시물이 존재하지 않습니다.</div>");
        }
      },
  
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        // alert('ajax야 힘내자'+ request +status + error);
      }
  });
}

//페이징 ajax
function view01(j) {    

  let postGroupId = sessionStorage.getItem("postGroupId");
  var msg = '';

  $.ajax({
    url: 'http://www.palette-travel.com/api/postgroup/'+ postGroupId+'?page='+j, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: '',      // 데이터
  
    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
            
    if (testData.storyLists.length > 0) { 
  
    //div 초기화 
      $('#card-image').empty();       
  
      for(i=0; i < testData.storyLists.length; i++ ){
  
        msg += '<div class="inner">'
        msg += '<div onclick="fnMyblogDetail(this.id);" id="' + testData.storyLists[i].postId + '">'
        msg += '<figure class="image">'
        msg += '<img src='  
        msg +=  testData.storyLists[i].thumbNailFullPath 
        msg += ' /></figure></div></div>';
        console.log(testData.storyLists[i].postId);
      }
      $('.card-image').html(msg);

    } else {  

    }
    },
  
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      console.log('ajax야 힘내자'+ request +status + error);
    }
  });
}
 
function page(){
  
  var pageCount;	
  let postGroupId = sessionStorage.getItem("postGroupId");
  $.ajax({
    url: 'http://www.palette-travel.com/api/page/post?postGroupId='+postGroupId,   
    type: 'GET',
    dataType: 'json',
    success: function(data){
      console.log("data: "+data)
      pageCount = data.data;
      console.log(pageCount);
  
      var html = "";
      for(var i=1; i<=pageCount; i++){			
        console.log(i);
        html += "<input type="+"'button" +"' class = paginate_num  value = " + i+" onclick="+ "view01("+i+");>";
      }

      $(".paginate").html(html);   

    }
  });
  return pageCount;
}

// myblogDetail 페이지 GroupPost 정보 출력
function view02(){
  var frm = $('#frmMyBlogDetail');

  var params = '';
  var msg = '';
  let postGroupId = sessionStorage.getItem("postGroupId");
  let memberId = sessionStorage.getItem("memberId");
  var page = sessionStorage.getItem("page");

  if(page >1 ){
    console.log(page);
    $.ajax({
      url: 'http://www.palette-travel.com/api/postgroup?memberId='+ memberId+'&page='+page, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터

      success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
        console.log(Data);
        for(i=0; i<Data.postGroupResponses.length; i++){

          if(postGroupId == Data.postGroupResponses[i].postGroupId){
            console.log(Data.postGroupResponses[i]);
            var data = Data.postGroupResponses[i];
          }
        }
        console.log(data);
        $("#title").append("<div> TITLE: "+ data.title +"</div>");
      },
  
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          // alert('ajax야 힘내자'+ request +status + error);
        }
    });
  }else{
    $.ajax({
      url: 'http://www.palette-travel.com/api/postgroup?page=1&memberId='+ memberId, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터

      success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
        for(i=0; i<Data.postGroupResponses.length; i++){

          if(postGroupId == Data.postGroupResponses[i].postGroupId){
            console.log(Data.postGroupResponses[i]);
            var data = Data.postGroupResponses[i];
        
          }
        }
        console.log(data);
        $("#title").append("<div> TITLE: "+ data.title +"</div>");
      },
  
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          // alert('ajax야 힘내자'+ request +status + error);
        }
    });
  }
}