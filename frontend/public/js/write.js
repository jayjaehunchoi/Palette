var sel_files = [];

$(document).ready(function(){
    $("#thumbNailFullPath").on("change",handleImgsFilesSelect);
});

function fileUploadAction(){
    console.log("fileUploadAction");
    $("#thumbNailFullPath").trigger('click');
}

function handleImgsFilesSelect(e){

//이미지 정보들을 초기화
  sel_files = [];
  $(".imgs_wrap").empty();

  var files = e.target.files;
  var filesArr = Array.prototype.slice.call(files);
  
  filesArr.forEach(function(f) {
    if(!f.type.match("image.*")){
      alert("확장자는 이미지 확장자만 가능합니다.");
      return;
    }
    sel_files.push(f);

    var reader = new FileReader();
    reader.onload = function(e){
    var img_html = "<img src = \"" + e.target.result + "\" />";
      $(".imgs_wrap").append(img_html);
   }
    reader.readAsDataURL(f);
  });
}

//ajax 요청 (글작성)
function add() {
  var frm = $('#myblogDetail')[0];
  var title = $('#title',frm).val();
  var content = $('#content ',frm).val();
  var thumbNailFullPath = $('#thumbNailFullPath ',frm);
   
  let token = sessionStorage.getItem("token"); 
  let postGroupId = sessionStorage.getItem("postGroupId");

  var params = { title : title , content : content };  // 개발시 변경 부분
   
  var formData = new FormData(frm);
  console.log(params);
  console.log(thumbNailFullPath[0].files[0]);
  formData.append('files',thumbNailFullPath[0].files[0]);
  formData.append('data',new Blob([JSON.stringify(params)] , {type: "application/json"}))   
   
  $.ajax({
    url: 'http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/postgroup/'+postGroupId+'/post', // 개발시 변경 부분
    contentType: false,
    Accept: 'application/json',
    processData:false,
    type: 'post',  // get, post
    cache: false, // 응답 결과 임시 저장 취소
    async: true,  // true: 비동기 통신
    data: formData,      // 데이터
   
    success: function(testData) { // 서버로부터 성공적으로 응답이 온경우
      window.location.href = "/view/Board/myblogDetail.html"
      if (testData != null) {    
        console.log(testData);   
      } else {  
           
      }

    },   
    // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
    error: function(request, status, error) { // callback 함수
      alert('입력 정보를 확인하세요.(제목 20자, 글 150자 이내)');
      console.log('ajax야 힘내자'+ request +status + error);
    },
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);
    }
  });
}