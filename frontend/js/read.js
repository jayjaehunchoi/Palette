    // 좋아요 버튼

    $(document).ready(function(){
        $('.like-content').one('click','.like-review',function(e){
            $(this).html('<i class = "far fa-heart" aria-hidden = "true"></i>  <span class="material-icons" style="color:red">favorite</span>');
            $(this).children('fa-heart').addClass('animate-like');
        });
    });

    //swiper

    var swiper= new Swiper('.swiper-container', {
        //기본 셋팅 //방향 셋팅 vertical 수직, horizontal 수평 설정이 없으면 수평 
        irection: 'horizontal', 
        //한번에 보여지는 페이지 숫자 
        //slidesPerView: 1, 
        //페이지와 페이지 사이의 간격 
        spaceBetween: 30,
        //드레그 기능 true 사용가능 false 사용불가 
        debugger: true, 
        //마우스 휠기능 true 사용가능 false 사용불가 
        //mousewheel: true, 
        //반복 기능 true 사용가능 false 사용불가 
        loop: false, 
        //선택된 슬라이드를 중심으로 true 사용가능 false 사용불가 djqt 
        centeredSlides: true, 
        // 페이지 전환효과 slidesPerView효과와 같이 사용 불가 
        // effect: 'fade', 


        //자동 스크를링 
        // autoplay: { //시간 1000 이 1초 
        //     delay: 2500, 
        //     disableOnInteraction: false, 
        // }, 
        
        //페이징 
        pagination: { 
            //페이지 기능 
        el: '.swiper-pagination',
         //클릭 가능여부 
         clickable: true, 
        }, 
        
        //방향표 
        navigation: { 
        //다음페이지 설정 
        nextEl: '.swiper-button-next', 
        //이전페이지 설정 
        prevEl: '.swiper-button-prev', 
    } 

});
        

// 답글
    $("#answerFrm").hide();
 
    $(document).ready(function() {
     
        $("#answer").click(function() {
            
            $("#answerFrm").show();
                
        });
        
         
        // $("#answerBtn").click(function() {
        //     alert("answer버튼 클릭");
        // });
         
    });

    // $('#btnLike').click(function ()  {
        
    //     $.ajax({
    //     url:"CommuBbsController",
    //     data: {command: 'like', seq: ${comdto.seq }, userid: ${current_user.seq }},
    //     type:"post",
    //     success : function (data) {
    
    //       var result = JSON.parse(data);
    
    //       if(result.status == 404){
    //       $('img#like_img').attr('src', './img/empty_heart.png');
    //       } else {
    //       $('img#like_img').attr('src', './img/heart.png');
    //       }
    //       $('span#like_count').html(result.like_count);
    //     })
    // });
