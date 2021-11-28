/* Login */

$('.loginBt').click(function () {
  var userId = $('.userId').val();
  var userPw = $('.userPw').val();
  var loginData = {
    "email": userId,
    "password": userPw
  };
  if (userId == '') {
    alert('아이디를 입력하세요.')
  } else if (userPw == '') {
    alert('비밀번호를 입력하세요')
  } else {
    $.ajax({
      type: "POST",
      url: "http://www.palette-travel.com/api/signin",
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
          sessionStorage.setItem('userName', userName);
          alert(userName + "님 환영합니다.");
          window.location.href = '/index.html';
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        let errorMsg = jqXHR.responseText.split("\"")[3];
        alert(errorMsg);
      }
    });
  }
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

  if (userId == '' || userName == '') {
    alert('필요한 정보를 모두 입력하세요.')
  } else {
    $.ajax({
      type: "POST",
      url: "http://www.palette-travel.com/api/sendEmail",
      contentType: "application/json",
      data: JSON.stringify(findInfo),
      success: function (response) {
        alert('임시비밀번호가 발송되었습니다.');
        window.location.href="login.html"; 
      },
      error: function (jqXHR) {
        let errorMsg = jqXHR.responseText;
        alert(errorMsg);
      }
    });
  }
});
