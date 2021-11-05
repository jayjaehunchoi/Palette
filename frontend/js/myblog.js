    // 추가하기 (여행일정)
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

    // 좋아요 버튼

        $(document).ready(function(){
            $('.like-content').one('click','.like-review',function(e){
                $(this).html('<i class = "far fa-heart" aria-hidden = "true"></i> You liked');
                $(this).children('fa-heart').addClass('animate-like');
            });
        });
 