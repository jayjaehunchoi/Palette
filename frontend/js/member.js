$('.joinBt').click(function() { 
  var userId = $('.userId').val(); 
  var userPw = $('.userPw').val();
  var userPw = $('.userPw').val(); 
  var userPw = $('.userPw').val();  

  $.ajax({ 
    type : "POST", 
    url : "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/signup", 
    data : {email:userId, password:userPw}, 
    success : function(data) { 
      if (data == "false") { 
        alert('아이디 혹은 비밀번호를 확인하세요') 
      } else { 
        window.location.href = '/frontend/index.html'; 
      } 
    }
  })
});



const selectBt = document.querySelector('.selectBt');
const profileFile = document.querySelector('.profileFile');

selectBt.addEventListener('click',()=>{
	profileFile.click();
});

/*
// 이미지 업로드
$('.profileFile').on('change', function() {
  
  ext = $(this).val().split('.').pop().toLowerCase(); //확장자
  
  if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
      window.alert('이미지 파일이 아닙니다!');
  } else {
      file = $('.profileFile').prop("files")[0];
      blobURL = window.URL.createObjectURL(file);
      $('.preview img').attr('src', blobURL);
      $('.preview').slideDown(); //업로드한 이미지 미리보기 
  }
  });
*/


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
            console.log(window.URL.createObjectURL(profile))
            console.log(profile)
          }
      }
    }
  };