//ajax 요청 (글 목록)
function view(){
  var frm = $('#frmStory');
  var page = $('#page',frm).val();
  let postGroupId = sessionStorage.getItem("postGroupId");
  var params = '';
  var msg = '';
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
        $('#card-image').empty();       
 
        for(i=0; i < testData.storyLists.length; i++ ){

          msg += '<div class="inner">'
          msg += '<div onclick="fnMyblogDetail(this.id);" id="' + testData.storyLists[i].postId + '">'
          msg += '<figure class="image">'
          msg += '<img src='  
          msg +=  testData.storyLists[i].thumbNailFullPath 
          msg += ' /></figure></div></div>';
          
        }
        $('.card-image').html(msg);
  
      } else {  
             
        $('.card-image').empty();         
        $('.card-image').append("<div class='material-icons'>error_outline</div><div class = 'notPost'>게시물이 존재하지 않습니다.</div>");
        
      }
    },
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        
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
          
        }
        $('.card-image').html(msg);
               
      } else {  
        testData.storyLists[0].postGroupId.value();  
        alert('내용 xx' + testData.storyLists[0].postGroupId.val());
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
     
      pageCount = data.data;
    
  
      var html = "";
        for(var i=1; i<=pageCount; i++){			
          
          html += "<input type="+"'button" +"' class = paginate_num  value = " + i+" onclick="+ "view01("+i+");>";
        }

      $(".paginate").html(html);   

    }
  });
  return pageCount;
}
      
// storyDetail 페이지 GroupPost 정보 출력
function view02(){

  var params = '';
  var msg = '';
  let postGroupId = sessionStorage.getItem("postGroupId");
  var page = sessionStorage.getItem("page");

  if(page >1 ){
    console.log(page);
    $.ajax({
      url: 'http://www.palette-travel.com/api/postgroup?page='+page, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터
      success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
      
        for(i=0; i<Data.postGroupResponses.length; i++){

          if(postGroupId == Data.postGroupResponses[i].postGroupId){
          
            var data = Data.postGroupResponses[i];
          }
        }
      
        $("#title").append("<div> TITLE: "+ data.title +"</div>");
      },
  
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
       
      }
    });
  }else{
    $.ajax({
      url: 'http://www.palette-travel.com/api/postgroup?page=1', // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터
      success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
        for(i=0; i<Data.postGroupResponses.length; i++){

          if(postGroupId == Data.postGroupResponses[i].postGroupId){
          
            var data = Data.postGroupResponses[i];
          }

        }
      
        $("#title").append("<div> TITLE: "+ data.title +"</div>");
      },  
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        
      }
    });
  }
}