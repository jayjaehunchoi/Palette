/* Login */

$('.loginBt').click(function () {
  var userId = $('.userId').val();
  var userPw = $('.userPw').val();
  var loginData = {
    "email": userId,
    "password": userPw
  };
  $.ajax({
      type: "POST",
      url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/signin",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      dataType: 'text',
      success: function (response ,status, jqXHR) {
        let token = JSON.parse(response).accessToken.value;
        let memberId = JSON.parse(response).memberId;
        let userName = JSON.parse(response).name;
        if (window.sessionStorage) { 
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('memberId', memberId);
          alert(userName + "님 환영합니다.");
          window.location.href = '/index.html';
        }
      },
        error: function (jqXHR, textStatus, errorThrown) {
          let errorMsg = jqXHR.responseText.split("\"")[3];
          alert(errorMsg);
        }
      })
  });

    
$('.userPw').keydown(function(key) {
  if(key.keyCode == 13) {
      $('.loginBt').click() ;
  }
});



/* FIND PW */
$('.findPwBt').click(function() {
  var userId = $('.findInfo .userId').val();
  var userName = $('.findInfo .userName').val();
  var findInfo = {
    "email": userId,
    "name": userName
  };
  $.ajax({
    type: "POST",
    url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/sendEmail",
    contentType: "application/json",
    data: JSON.stringify(findInfo),
    success: function (response) {
      alert('임시비밀번호가 발송되었습니다');
      window.location.href="login.html"; 
    },
    error: function (jqXHR) {
      let errorMsg = jqXHR.responseText;
      alert(errorMsg);
    }
  })
})
