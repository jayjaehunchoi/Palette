// ajax 요청 (글목록)
function view(){
  var frm = $('#frmmodifymyblog');
  var startDate = $('#startDate',frm).val();
  var endDate = $('#endDate ',frm).val();
  var title = $('#title ',frm).val();
  var region = $('#region ',frm).val();
  var thumbNailUrl = $('#Thumbnail ',frm);

  let token = sessionStorage.getItem("token");
  let postGroupId = sessionStorage.getItem("postGroupId");
  let memberId = sessionStorage.getItem("memberId");

  $.ajax({
    url: 'http://www.palette-travel.com/api/postgroup?memberId='+memberId, // 개발시 변경 부분
    contentType: 'application/json; charset=UTF-8',
    type: 'get',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    dataType: 'json', // 응답 형식: json, html, xml...
    data: '',      // 데이터

    success: function(Data) { // 서버로부터 성공적으로 응답이 온경우
      console.log(Data.postGroupResponses.length);
      for(i=0; i<Data.postGroupResponses.length; i++){

        if(postGroupId == Data.postGroupResponses[i].postGroupId){
          console.log(Data.postGroupResponses[i]);
          var data = Data.postGroupResponses[i];
        }
      }

      console.log(data);

      $('#startDate').val(data.period.startDate);
      $('#endDate').val(data.period.endDate);
      $('#title').val(data.title);
      $('#region').val(data.region);
      $('#Thumbnail').attr("src",data.thumbNailUrl);

      console.log(data.thumbNailUrl);
 
    },

    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);
    },

    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      console.log('ajax야 힘내자'+ request +status + error);
      console.log(formData);
    }
  });
}

// ajax 요청 (글수정)
//debugger;     
function modify() {
  var frm = $('#frmmodifymyblog')[0];
  var startDate = $('#startDate',frm).val();
  var endDate = $('#endDate ',frm).val();
  var title = $('#title ',frm).val();
  var region = $('#region ',frm).val();
  var Thumbnail = $('#Thumbnail ',frm);

  let token = sessionStorage.getItem("token");
  let postGroupId = sessionStorage.getItem("postGroupId");

  var params = {period:{startDate : startDate ,endDate : endDate}, title : title , region : region };  // 개발시 변경 부분

  var formData = new FormData(frm);
    console.log(params);
    console.log(Thumbnail[0].files[0]);
    formData.append('file',Thumbnail[0].files[0]);
    formData.append('data',new Blob([JSON.stringify(params)] , {type: "application/json"}));

  $.ajax({
    url: 'http://www.palette-travel.com/api/postgroup/'+ postGroupId, // 개발시 변경 부분
    contentType: false,
    Accept: 'application/json',
    processData:false,
    type: 'PUT',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    // dataType: 'json', // 응답 형식: json, html, xml...
    data: formData,      // 데이터

    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
      alert("수정이 완료되었습니다.");  
      window.location.href = '/view/Board/myblog.html'; 
          
      if (testData != null) { 
        console.log(testData);
      } else {  

      }
      // window.location.href = "./myblog.html";
    },
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);
    },

    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      console.log('ajax야 힘내자'+ request +status + error);
      console.log(formData);
      alert("입력 정보를 확인하세요.")
    }
  });
}