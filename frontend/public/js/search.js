/**
 * 슬라이드 요소 관리
 */
 swiperTabsNav = new Swiper('.swiper-tabs-nav', {
  spaceBetween: 0, //슬라이드 사이 여백
  slidesPerView: 3,
  loop: false, // 반복 재생 여부
  loopedSlides: 5,
  autoHeight: false,
  resistanceRatio: 0, 
  watchOverflow: true, 
  watchSlidesVisibility: true,
  watchSlidesProgress: true,
});

// Swiper Content
swiperTabsContent = new Swiper('.swiper-tabs-content', {
  spaceBetween: 0, // 슬라이드 사이 여백
  loop:false, // 반복 재생 여부
  autoHeight: false,
  longSwipes: true,
  resistanceRatio: 0, 
  watchOverflow: true, 
  loopedSlides: 5,
  touchRatio : 0,
  thumbs: {
    swiper: swiperTabsNav,
  },
});


/**
 * 검색 데이터 연결
 */
$(document).ready(function() {
  var keyword =  sessionStorage.getItem("keyword");
  $(".keyword").val(keyword);
  search(keyword);
  // 페이징
  pageBlogPost();
  pageUsername();
  var currentPage = 1;
  var currentPage2 =1;
  sessionStorage.setItem("currentPage", currentPage);
  sessionStorage.setItem("currentPage2", currentPage2);
});

// 클릭 검색
$(".search-box .material-icons").click(function() {
  $("#all-blog-posts-results").html("")
  $("#all-username-results").html("")
  $("#blog-posts-results").html("")
  $("#username-results").html("")
  var keyword = $("#keyword").val();
  sessionStorage.setItem("keyword", keyword);
  pageBlogPost();
  pageUsername();
  var currentPage = 1;
  var currentPage2 =1;
  sessionStorage.setItem("currentPage", currentPage);
  sessionStorage.setItem("currentPage2", currentPage2);
  
      
// 클릭 시 알림창
if (keyword == "") {
    alert('검색어를 입력하세요.');
  } else {
    search(keyword);
    }
});

// 엔터 검색
$('.keyword').keydown(function(key) {
  var keyword = $(".keyword").val();
  if(key.keyCode == 13) {
    $("#all-blog-posts-results").html("")
    $("#all-username-results").html("")
    $("#blog-posts-results").html("")
    $("#username-results").html("")
    sessionStorage.setItem("keyword", keyword);
    search(keyword);
    // 페이징
    pageBlogPost();
    pageUsername();
    var currentPage = 1;
    var currentPage2 =1;
    sessionStorage.setItem("currentPage", currentPage);
    sessionStorage.setItem("currentPage2", currentPage2);
  }
});

function search(keyword) {
  // ajax 통신
  // title
  $.ajax({ // 요청에 대한 정보
    type: "GET", // 전송방식. HTTP method type(GET, POST) 형식
    url: "http://www.palette-travel.com/api/postgroup?title=" + keyword, // 전송주소. 컨트롤러에서 대기중인 url 주소
    dataType : "json",
    ContentType: "application/json; charset=UTF-8",
    success: function(res){ //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      console.log(keyword);
      var result =  res.postGroupResponses;

      if(result != null) {
        for (var i = 0; i < result.length; i++) {
        console.log(res.postGroupResponses[i].thumbNailUrl);
        $("#all-blog-posts-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        $("#blog-posts-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        }
      }
    },
    error: function(XMLHttpRequest, textStatus, errorThrown) { //비동기 통신이 실패할경우 error 콜백으로 들어옴.
      console.log("통신 실패")
    }
  });

  // memberName
  $.ajax({ // 요청에 대한 정보
    type: "GET", // 전송방식. HTTP method type(GET, POST) 형식
    url: "http://www.palette-travel.com/api/postgroup?name=" + keyword, // 전송주소. 컨트롤러에서 대기중인 url 주소
    dataType : "json",
    ContentType: "application/json; charset=UTF-8",
    success: function(res){ //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      console.log(keyword);
      var result =  res.postGroupResponses;
      
      if(result != null) {
        for (var i = 0; i < result.length; i++) {
        console.log(res.postGroupResponses[i].thumbNailUrl);
        $("#all-username-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        $("#username-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        }
      }
    },
    error: function(XMLHttpRequest, textStatus, errorThrown) { //비동기 통신이 실패할경우 error 콜백으로 들어옴.
      console.log("에러 발생!")
    }
  });
}


/**
 * 검색페이지 페이징
 */
// Blogposts paging
function viewBlogPosts(pageNum) {    
  let keyword = sessionStorage.getItem("keyword");

  $.ajax({ // 요청에 대한 정보
    url: "http://www.palette-travel.com/api/postgroup?title="+ keyword+'&page='+pageNum, // 전송주소. 컨트롤러에서 대기중인 url 주소
    type: "GET", //전송방식. HTTP method type(GET, POST) 형식
    dataType: "json", 
    ContentType: "application/json; charset=UTF-8",
  
    success: function(res) { //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      var result =  res.postGroupResponses;
      $("#blog-posts-results").html("")
      if(result != null) {
        for (var i = 0; i < result.length; i++) {
        $("#blog-posts-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        }        
      }
    },
    error: function(request, status, error) { //비동기 통신이 실패할경우 error 콜백으로 들어옴.
      console.log('에러 발생!');
    }
  });
}

function pageBlogPost(){
  var searchPageCount;	
  let keyword = sessionStorage.getItem("keyword");
  $.ajax({ // 요청에 대한 정보
    url: "http://www.palette-travel.com/api/page/postgroup?title="+keyword, // 전송주소. 컨트롤러에서 대기중인 url 주소  
    type: "GET", // 전송방식. HTTP method type(GET, POST) 형식
    dataType: "json",
    success: function(res){ //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      console.log("data: "+res)
      searchPageCount = res.data;
      sessionStorage.setItem("searchPageCount", searchPageCount);  
    }
  });
}


// Username paging
function viewUsername(pageNum) {    
  let keyword = sessionStorage.getItem("keyword");
  $.ajax({ // 요청에 대한 정보
    url: "http://www.palette-travel.com/api/postgroup?name="+ keyword+'&page='+pageNum, // 전송주소. 컨트롤러에서 대기중인 url 주소
    type: "GET",  // 전송방식. HTTP method type(GET, POST) 형식
    dataType: "json",
    ContentType: "application/json; charset=UTF-8",
  
    success: function(res) { //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      var result =  res.postGroupResponses;
      $("#username-results").html("")
      if(result != null) {
        for (var i = 0; i < result.length; i++) {
        console.log(res.postGroupResponses[i].thumbNailUrl);
        $("#username-results").append("<div onclick='moveToPostGroup(this.id)' class='search-post' id='" +res.postGroupResponses[i].postGroupId + "'><img src='" +  res.postGroupResponses[i].thumbNailUrl + "'/></div>");
        }        
      }
    },
    error: function(request, status, error) { //비동기 통신이 실패할경우 error 콜백으로 들어옴.
      console.log('에러 발생!');
    }
  });
}

function pageUsername(){
  var searchPageCount2;	
  let keyword = sessionStorage.getItem("keyword");

  $.ajax({ // 요청에 대한 정보
    url: "http://www.palette-travel.com/api/page/postgroup?name="+keyword, // 전송주소. 컨트롤러에서 대기중인 url 주소 
    type: "GET", // 전송방식. HTTP method type(GET, POST) 형식
    dataType: "json",
    success: function(res){ //비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터.
      console.log("data: "+res)
      searchPageCount2 = res.data;
      sessionStorage.setItem("searchPageCount2", searchPageCount2); 
    }
  });
}

// blog posts의 페이지 이동
$(".paginate-btn-back").click(function() {
  var currentPage =  sessionStorage.getItem("currentPage");
  if(currentPage == 1) {
    alert("첫 페이지입니다.");
  }else {
    var currentPage = parseInt(currentPage) -1;
    sessionStorage.setItem("currentPage", currentPage);
    viewBlogPosts(currentPage);
  }
  
});
$(".paginate-btn-forward").click(function() {
  var currentPage =  sessionStorage.getItem("currentPage");
  var searchPageCount =  sessionStorage.getItem("searchPageCount");
  if(currentPage == searchPageCount) {
    alert("마지막 페이지입니다.");
    viewBlogPosts(currentPage);
  }else {
    var currentPage = parseInt(currentPage) +1;
    sessionStorage.setItem("currentPage", currentPage);
    viewBlogPosts(currentPage);
  }
});

// username의 페이지 이동
$(".paginate-btn-back2").click(function() {
  var currentPage2 =  sessionStorage.getItem("currentPage2");
  if(currentPage2 == 1) {
    alert("첫 페이지입니다.");
  }else {
    var currentPage2 = parseInt(currentPage2) -1;
    sessionStorage.setItem("currentPage2", currentPage2);
    viewUsername(currentPage2);
  }
  
});
$(".paginate-btn-forward2").click(function() {
  var currentPage2 =  sessionStorage.getItem("currentPage2");
  var searchPageCount2 =  sessionStorage.getItem("searchPageCount2");
  if(currentPage2 == searchPageCount2) {
    alert("마지막 페이지입니다.");
    viewUsername(currentPage2);
  }else {
    var currentPage2 = parseInt(currentPage2) +1;
    sessionStorage.setItem("currentPage2", currentPage2);
    viewUsername(currentPage2);
  }
});