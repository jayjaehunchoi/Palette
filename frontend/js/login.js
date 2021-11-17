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
          url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/signin",
          contentType: "application/json",
          data: JSON.stringify(loginData),
          dataType: 'text',
          xhrFields : {withCredentials : true},
          crossDomain: true,
          success: function (response ,status, jqXHR) {
            console.log(jqXHR);
            console.log(jqXHR.getAllResponseHeaders());
            //console.log(response.getResponseHeader("Set-Cookie"));
            if (window.sessionStorage) { //세션관리는 위한 세션 설정해주는 부분.
              sessionStorage.setItem('id', document.querySelector(".userId").value);
              var userId = sessionStorage.getItem('id');
              console.log(userId);
              alert(userId + "님 환영합니다.");
            }
          },
            error: function (jqXHR, textStatus, errorThrown) {
              let errorMsg = jqXHR.responseText.split("\"")[3];
              alert(errorMsg);
            }
          })
      });

    // $('.loginBt').click(function() { 
    //   // var userId = $('.userId').val(); 
    //   // var userPw = $('.userPw').val(); 
    //   fetch("http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/signin", {
    //     method: "POST",
    //     headers: {
    //       "Content-Type": "application/json",
    //     },
    //     body: JSON.stringify({
    //       email: "email",
    //       password: "password",
    //     }),
    //   }).then((response) => console.log(response));

    // });

    /*
    $('.loginInfo').keydown(function(key) {
      if(key.keyCode == 13) {
          $('#loginBt').click() ;
      }
    });
    */

    $('.logout').click(function () {
      $.ajax({  
      type: "GET",
      url: "http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/signout",
      contentType: "application/json",
      data: document.cookie,
      dataType: 'text',
      xhrFields : {withCredentials : true},
      crossDomain: true,
      success: function (response ,status, jqXHR) {
          window.location.href = "/frontend/index.html"
        },

      error: function (jqXHR, textStatus, errorThrown) {
          console.log(jqXHR);
        }
    
    })
  });