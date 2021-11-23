// ajax 요청 

function view(){
  var frm = $('#frmMyBlogDetail');
  var page = $('#page',frm).val();

  var params = '';
  var msg = '';
  let postGroupId = sessionStorage.getItem("postGroupId");

  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/'+ postGroupId, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: params,      // 데이터

    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
            
      if (testData.storyLists.length > 0) { 
              
      //div 초기화
      $('.card-image').empty();       

        for(i=0; i < testData.storyLists.length; i++ ){
         // $("#card-image").append("<div><figure class='image'><img src=' " + testData.storyLists[i].thumbNailFullPath + " '/></figure></div>");
          msg += '<div onclick="fnMyblogDetail(this.id);" id="' + testData.storyLists[i].postId + '">'
          msg += '<figure class="image">'
          msg += '<img src='  
          msg +=  testData.storyLists[i].thumbNailFullPath 
          msg += ' /></figure></div>';
          console.log(testData.storyLists[i].postId);
        }
        $('.card-image').html(msg);

            } else {  
              testData.storyLists[0].postGroupId.value();
  
              alert('내용 xx' + testData.storyLists[0].postGroupId.val());
            }
          },
  
          // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
          error: function(request, status, error) { // callback 함수
            alert('ajax야 힘내자'+ request +status + error);
          }
        });
  }

//페이징 ajax
  function view01(j) {    
    var frm = $('#frmMyStoryGroup');
  
    $.ajax({
      url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/11?page='+j, // 개발시 변경 부분
      contentType: 'application/json; charset=UTF-8',
      type: 'get',  // get, post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: '',      // 데이터
  
      success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
            
        if (testData.postGroupResponses.length > 0) { 
  
          //div 초기화 
          $('#card-image').empty();       
  
          for(i=0; i < testData.postGroupResponses.length; i++ ){
  
  
          }   
               
        } else {  
          //estData.storyLists[0].postGroupId.value();
  
          //alert('내용 xx' + testData.storyLists[0].postGroupId.val());
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

      $.ajax({
        url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/page/post',   
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
