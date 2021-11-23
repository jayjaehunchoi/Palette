// 추가하기 (여행일정) - 모달창
  var modal = document.querySelector("#t_space .modal"); 
  var btn = document.querySelector(".btn"); 
  var closeButton = document.querySelector("#t_space .close-button"); 
  // var cancelButton = document.querySelector("#cancel");
    
  function toggleModal() { 
    modal.classList.toggle("show-modal"); 
  }
    
  function windowOnClick(event) { 
    if (event.target === modal) { 
      toggleModal(); 
    } 
  }
    
  btn.addEventListener("click", toggleModal); 
  closeButton.addEventListener("click", toggleModal); 
  cancel.addEventListener("click", toggleModal); 
  window.addEventListener("click", windowOnClick); 
    
  // var submitButton = document.querySelector("#submit");
  //submit.addEventListener("click",toggleModal);
    
  function add_textbox(){
    document.getElementById("t_space").innerHTML += 
    "<div style = 'float:left'><div style = 'margin-left:30px'><div id='boxWrap'><p class='original'>등장</p></div>"
   }

//ajax 요청 (목록 출력)

  function view() {

    var frm = $('#frmMyStoryGroup');
        
    var token = sessionStorage.getItem("token");
        
       $.ajax({
        url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/my', // 개발시 변경 부분
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
            $('#card-content').empty();       
            $('#member_id').empty();
        
            $("#member_id").append("<div>"+testData.postGroupResponses[0].memberName+"`s STORY </div>");
        
              for(i=0; i < testData.postGroupResponses.length; i++ ){
        
                 $("#card-image").append("<div class = 'inner'><div class = 'fnMyblogDetail' onclick='fnMyblogDetail(this.id)'; id=" +testData.postGroupResponses[i].postGroupId +">\
                  <figure class='image'><img src=' " + testData.postGroupResponses[i].thumbNailUrl + " '/></figure>\
                  </br><div class='card-content'><div class='content'><h5>" + testData.postGroupResponses[i].title + "</h5>\
                  <h5>" + testData.postGroupResponses[i].period.startDate + "~" + testData.postGroupResponses[i].period.endDate + "</h5>\
                  <h5>" + testData.postGroupResponses[i].region + "</h5></div></div></div>\
                  <input type='button' class = 'modify' value='수정' onclick='modifyPost(this.id)'; id=" +testData.postGroupResponses[i].postGroupId +">\
                  <input type='button'  class = 'delete' value='삭제' onclick='deletePost(this.id)'; id=" +testData.postGroupResponses[i].postGroupId +"></div>"
                  );

                  console.log(testData.postGroupResponses[i].postGroupId);
              }  
    
        
            } else {  
                testData.postGroupResponses[0].postGroupId.value();
        
                alert('내용 xx' + testData.postGroupResponses[0].postGroupId.val());
                }
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
        
// ajax 요청 (글작성)            

  //debugger;     
  function add() {
    var frm = $('#frmaddmyblog')[0];
    var startDate = $('#startDate',frm).val();
    var endDate = $('#endDate ',frm).val();
    var title = $('#title ',frm).val();
    var region = $('#region ',frm).val();
    var Thumbnail = $('#Thumbnail ',frm);
   
    let token = sessionStorage.getItem("token"); 
   
    var params = {period:{startDate : startDate ,endDate : endDate}, title : title , region : region };  // 개발시 변경 부분
   
    var formData = new FormData(frm);
      console.log(params);
      console.log(Thumbnail[0].files[0]);
      formData.append('file',Thumbnail[0].files[0]);
      formData.append('data',new Blob([JSON.stringify(params)] , {type: "application/json"}))   
   
      $.ajax({
         url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup', // 개발시 변경 부분
         contentType: false,
         Accept: 'application/json',
         processData:false,
         type: 'post',  // get, post
         cache: false, // 응답 결과 임시 저장 취소
         async: true,  // true: 비동기 통신
         dataType: 'json', // 응답 형식: json, html, xml...
         data: formData,      // 데이터
   
         success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
             
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
   
// 페이징 ajax 

function view01(j) {    
  var frm = $('#frmMyStoryGroup');
  let memberId = sessionStorage.getItem("memberId");

  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/?memberId='+memberId+'&page='+j, // 개발시 변경 부분
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
            <h5>" + testData.postGroupResponses[i].region + "</h5></div></div></div>\
            <input type='button' class = 'modify'  id='modify' value='수정' onclick='modifyPost()'>\
            <input type='button'  class = 'delete' id='delete' value='삭제' onclick='deletePost()'></div>"
          );
  
        }   
               
      } else {  
        alert('내용 xx' + testData.postGroupResponses[0].postGroupId.val());
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
  let memberId = sessionStorage.getItem("memberId");

  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/page/postgroup?memberId='+ memberId,   // 수정해야됨
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
