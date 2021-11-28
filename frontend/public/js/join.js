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
      reader.onload = function  () {
    
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
 
  $(".userId").on("change keydown paste input", function() {
    var currentVal = $(this).val();

    if( currentVal.indexOf("@") <= 0 || currentVal.indexOf(".") <= 0) {
      document.querySelector('#idChk').style.display="inline";
    } else {
      document.querySelector('#idChk').style.display="none";
    }
  });

  $(".userPw").on("change keydown paste input", function() {
    var reg = /^(?=.*[A-Za-z])(?=.*\d)(?=.*\W)(?=\S+$).{8,20}$/;
    var currentVal = $(this).val();

    if( !reg.test(currentVal)) {
      document.querySelector('#pwChk').style.display="inline";
    } else {
      document.querySelector('#pwChk').style.display="none";
    }
  });
   

  $('.joinBt').click(function() { 
    var userId = $('.userId').val(); 
    var userPw = $('.userPw').val();
    var userPwChk = $('.userPwChk').val(); 
    var userName = $('.userName').val();
    var profileFile = $('.profileFile');
    var memberData = {"name":userName, "password":userPw, "email":userId};
    let errorMsg = $('#overallChk');

    if (userId == '' || userPw == '' || userPwChk =='' || userName == '') {
      errorMsg.html('기본 정보를 모두 입력하세요');
    } else if (profileFile[0].files[0] == undefined) {
      errorMsg.html('프로필 사진을 선택하세요')
    } else if (document.querySelector('#idChk').style.display != "none"){
      errorMsg.html('이메일을 확인해주세요')
    } else if (document.querySelector('#pwChk').style.display != "none"){
      errorMsg.html('비밀번호를 확인해주세요')
    } else if (userPw != userPwChk) {
      errorMsg.html('비밀번호가 일치하지 않습니다');
    } else {
      var formData = new FormData();
      formData.append('member-data', new Blob([JSON.stringify(memberData)], {type: "application/json"}));
      formData.append('file', profileFile[0].files[0]);

      $.ajax({ 
        type : "POST", 
        url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/signup", 
        data: formData,
        Accept: 'application/json',
        contentType: false,
        processData: false,
        success : function(data) { 
          alert("회원 가입에 성공했습니다!");
          window.location.href="login.html";
        },
        error: function (jqXHR, textStatus, errorThrown) {
          let errorMsg = jqXHR.responseText.split("\"")[3];
          alert(errorMsg);
        }
     })
    }
  });
  
  
  