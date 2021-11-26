// ajax 요청    
function view() {
  var frm = $('#frmMyStoryGroup');
  var filter = $('#filter', frm).val();
  var condition = $('#condition', frm).val();

  var params = 'filter=' + filter +'&condition=' + condition;  // 개발시 변경 부분
  var msg = '';

  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup', // 개발시 변경 부분,
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터
    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
          
    if (testData.postGroupResponses.length > 0) { 

      $('#card-content').empty(); 
      $('#card-image').empty();       

      for(i=0; i < testData.postGroupResponses.length; i++ ){

        console.log(testData.postGroupResponses.length);
         
        $("#card-image").append("<div class = 'inner'><div class = 'fnMyblogDetail' onclick='fnMyblogDetail(this.id)'; id=" +testData.postGroupResponses[i].postGroupId +">\
          <figure class='image'><img src=' " + testData.postGroupResponses[i].thumbNailUrl + " '/></figure>\
          </br><div class='card-content'><div class='content'><h5>" + testData.postGroupResponses[i].title + "</h5>\
          <h5>" + testData.postGroupResponses[i].period.startDate + "~" + testData.postGroupResponses[i].period.endDate + "</h5>\
          <h5>" + testData.postGroupResponses[i].region + "</h5></div>\
          <p><span>"+testData.postGroupResponses[i].memberName+"</span></p></div></div>"
        );

       }   
      } else {  
        testData.postGroupResponses[0].postGroupId.value();
        alert('내용 xx' + testData.postGroupResponses[0].postGroupId.val());
      }
    },
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      alert('ajax야 힘내자'+ request +status + error);
    }
  });
}

// 페이징 ajax
function view01(j) {    
  var frm = $('#frmMyStoryGroup');
  sessionStorage.setItem("page", j);

  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup?page='+j, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: '',      // 데이터
    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
            
      if (testData.postGroupResponses.length > 0) { 
  
        //div 초기화
        $('#card-content').empty(); 
        $('#card-image').empty();       
  
        for(i=0; i < testData.postGroupResponses.length; i++ ){
  
          $("#card-image").append("<div class = 'inner'><div class = 'fnMyblogDetail' onclick='fnMyblogDetail(this.id)'; id=" +testData.postGroupResponses[i].postGroupId +">\
            <figure class='image'><img src=' " + testData.postGroupResponses[i].thumbNailUrl + " '/></figure>\
            </br><div class='card-content'><div class='content'><h5>" + testData.postGroupResponses[i].title + "</h5>\
            <h5>" + testData.postGroupResponses[i].period.startDate + "~" + testData.postGroupResponses[i].period.endDate + "</h5>\
            <h5>" + testData.postGroupResponses[i].region + "</h5></div>\
            <p><span>"+testData.postGroupResponses[i].memberName+"</span></p></div></div>"
          );
        }   
      } else {  
          $('#card-content').empty(); 
          $('#card-image').empty();
          $('#card-image').append("<div class='material-icons'>error_outline</div><div class = 'notPost'>게시물이 존재하지 않습니다.</div>");
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
  var currentPage = 1;
  
  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/page/postgroup',   
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
  
  
