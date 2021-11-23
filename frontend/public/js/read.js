    // 좋아요 버튼 
    $(".like-content .btn-secondary").click(function() {
        //$(this).toggleClass("done");
     let postId = sessionStorage.getItem("postId");
     let token = sessionStorage.getItem("token"); 
        if(token != null){
            $(this).toggleClass("done");
        }else{
            alert("로그인이 필요합니다");
        }

        $.ajax({
            url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/like",
            method:"POST",
            data: "",
            success:function(data){
                console.log(data);
                $('#result').text(data.data);
            },

beforeSend: function (xhr) {
  xhr.setRequestHeader("Authorization","Bearer " + token);
}
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

    // 댓글작성
    function submitComment(){

        var content = $('#new-comment').val();
        let token = sessionStorage.getItem("token");
        let postId = sessionStorage.getItem("postId");

        $.ajax({
            url:"http://ec2-3-35-87-7.ap-northeast-2.compute.amazonaws.com:8080/api/post/"+postId+"/comment",
            method:"POST",
            data:JSON.stringify({content:content}),
            dataType:'json',
            contentType: "application/json", 
            success:function(data){
                console.log(data);
            },
            error:function(e){
                 console.log("error : ", e);
                 },

                 beforeSend: function (xhr) {
                   xhr.setRequestHeader("Authorization","Bearer " + token);
                 }
        });

    }


