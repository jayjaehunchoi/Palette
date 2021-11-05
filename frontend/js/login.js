/* Login */

$('.loginBt').click(function() { 
  var userId = $('.userId').val(); 
  var userPw = $('.userPw').val(); 

  $.ajax({ 
    type : "POST", 
    url : "", 
    data : {uid:userId, upw:userPw}, 
    success : function(data) { 
      if (data == "false") { 
        alert('아이디 혹은 비밀번호를 확인하세요') 
      } else { 
        window.location.href = 'index.html'; 
      } 
    }
  })
});

/*
$('.loginInfo').keydown(function(key) {
  if(key.keyCode == 13) {
      $('#loginBt').click() ;
  }
});
*/