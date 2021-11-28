//마이페이지로 이동 시 회원정보 받아오기
$(document).ready(function(){
  var token = sessionStorage.getItem("token");
  $.ajax({
    type : "GET",
    url : "http://www.palette-travel.com/api/member",
    dataType : "json",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization","Bearer " + token);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      let errorMsg = jqXHR.responseText.split("\"")[3];
      alert(errorMsg);
      window.location.href="/index.html";
    },
    success: function(data){
      $('.userId').val(data.email);
      $('.userName').val(data.name);
      $('.preview img').attr("src", data.profile);
    }
  });
});

const selectBt = document.querySelector('.selectBt');
const profileFile = document.querySelector('.profileFile');

selectBt.addEventListener('click',()=>{
	profileFile.click();
});

profileFile.onchange = function () { 
  var profile = profileFile.files[0];
  
  ext = $(this).val().split('.').pop().toLowerCase(); //확장자

  if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
    window.alert('이미지 파일이 아닙니다!');
   
  } else {
    
    // 읽기
    var reader = new FileReader();
    reader.readAsDataURL(profile);
    //로드 한 후
    reader.onload = function() {
  
      var tempImage = new Image(); //drawImage 메서드에 넣기 위해 이미지 객체화
      tempImage.src = reader.result; //data-uri를 이미지 객체에 주입
      tempImage.onload = function() {
        //리사이즈를 위해 캔버스 객체 생성
        var canvas = document.createElement('canvas');
        var canvasContext = canvas.getContext("2d");
        
        //캔버스 크기 설정
        canvas.width = 225;
        canvas.height = 225; 
        
        //이미지를 캔버스에 그리기
        canvasContext.drawImage(this, 0, 0, 225, 225);
        //캔버스에 그린 이미지를 다시 data-uri 형태로 변환
        var dataURI = canvas.toDataURL("image/jpeg");
        
        //썸네일 이미지 보여주기
        document.querySelector('.preview img').src = dataURI;
      }
    }
  }
};

//회원정보수정
$('.modifyBt').click(function() {
  var token = sessionStorage.getItem("token");
  var userPw = $('.userPw').val();
  var userPwChk = $('.userPwChk').val();
  var profileFile = $('.profileFile');
  var memberUpdateData = {"password":userPw};
  var reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*\W)(?=\S+$).{8,20}$/;
  
  if (userPw == '' || userPwChk == '') {
    alert("비밀번호를 입력해주세요")
  } else if (!reg.test(userPw)) {
    alert("비밀번호는 영문+숫자+특수기호 조합의 8~20자로 입력해주세요");
  }else if (userPw != userPwChk) {
    alert("비밀번호가 일치하지 않습니다");
  } else if (profileFile[0].files[0] == undefined) {
    alert("프로필 사진을 선택해주세요")
  } else {
    var formData = new FormData();
    formData.append('member-update-data', new Blob([JSON.stringify(memberUpdateData)], {type: "application/json"}));
    formData.append('file', profileFile[0].files[0]);
    $.ajax({
      type: "PUT",
      url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/member",
      data: formData,
      Accept: 'application/json',
      contentType: false,
      processData: false,
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        let errorMsg = jqXHR.responseText;
        alert(errorMsg);
      },
      success: function(data){
        alert("회원정보가 수정되었습니다!");
        window.location.href="/index.html";
      }
    });
  }
});

// 모달 열기
function openPwChkModal() {
  document.getElementById("pwChkModal").style.display="block";
}

//모달 닫기
function closePwChkModal() {
  document.getElementById("pwChkModal").style.display="none";
  $('.withdrawalPwChk input').val('');
}   

//탈퇴버튼 클릭 시 비밀번호 확인창 오픈
$('.withdrawalBt').click(function() { 
  openPwChkModal();
});


//비밀번호확인창에서 확인버튼 클릭 시 
$('.modalBt').click(function() {
  var token = sessionStorage.getItem("token");
  var pw = $('.withdrawalPwChk input').val();

  if (confirm("정말 탈퇴하시겠습니까?")) {
    $.ajax({
      type: "DELETE",
      url: "http://www.palette-travel.com/api/member",
      data: JSON.stringify({"password":pw}),
      contentType: 'application/json',
      beforeSend: function (xhr) {
        xhr.setRequestHeader("Authorization","Bearer " + token);//header추가
      },
      error: function (jqXHR, textStatus, errorThrown) {
        let errorMsg = jqXHR.responseText.split("\"")[3];
        alert(errorMsg);
        $('.withdrawalPwChk input').val('');
      },
      success: function(data){
        alert("탈퇴되셨습니다");
        sessionStorage.clear();
        window.location.href="/index.html";
      }
    });
  } else {
    closePwChkModal();
  }
});