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

        // 데이터 전송
        var form = $('myblog')[0];
        var formData = new FormData(form);
        $.ajax({
	        type:"post",
	        enctype:'multipart/form-data',
            url:'',
            data:formData,
            dataType:'json',
            processData:false, // 프로세스데이터 설정 : false값을 해야 form data로 인식
            contentType:false, // 헤더의 Content_Type을 설정 : false 값을 해야 form data로 인식
            cache:false,
            success:function(data){
    	        console.log("success : ", data);
            },
            error:function(e){
                console.log("error : ", e);
             }
        });

//  postsSave : function () {
//     var data = {
//   title: $('#title').val(),
//         content: $('#content').val(),
//         email: $('#email').val()
//     };
//     var author = $('#author').val();
//     var form =$('#form')[0];
//     var formData = new FormData(form);
//     formData.append('file', $('#file'));
//     formData.append('key', new Blob([JSON.stringify(data)] , {type: "application/json"}));
    
//     $.ajax({
//         type: 'POST',
//         url: '/api/v1/'+author+'/posts',
//         processData: false,
//         contentType:false,
//         data: formData,
//     }).done(function() {
//         alert('글이 등록되었습니다.');
//         window.location.href = '/';
//     }).fail(function (error) {
//         alert(JSON.stringify(error));
//     });
// } 


