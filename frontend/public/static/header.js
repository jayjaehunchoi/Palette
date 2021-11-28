/**
 * 검색창 제어
 */
// 검색창 요소(.search) 찾기.
const searchEl = document.querySelector('.search');
const searchInputEl = searchEl.querySelector('input');
// 검색창 요소를 클릭하면 실행.
searchEl.addEventListener('click', function () {
  searchInputEl.focus();
});
// 검색창 요소 내부 실제 input 요소에 포커스되면 실행.
searchInputEl.addEventListener('focus', function () {
  searchEl.classList.add('focused');
  searchInputEl.setAttribute('placeholder', '통합검색');
});
// 검색창 요소 내부 실제 input 요소에서 포커스가 해제(블러)되면 실행.
searchInputEl.addEventListener('blur', function () {
  searchEl.classList.remove('focused');
  searchInputEl.setAttribute('placeholder', '');
});

// 페이지 이동 후 원하는 위치로 스크롤 이동
function otherPage(){
	$("html, body").scrollTop(0);
	if (window.location.hash) {
        setTimeout(function() {
        	var headerH = jQuery('header').outerHeight();
            $('html, body').animate({
                scrollTop: $(window.location.hash).offset().top -20
            }, 2000)
            window.location.hash = '/frontend/index.html#about_move';
            history.replaceState('', document.title, window.location.pathname);
        }, 100);
    }
}

$(document).ready(function() {
  // clickToScroll();
	otherPage();
});

$(document).ready(function() {
  var token = sessionStorage.getItem("token");
  if ( token ) {
    document.querySelector('div.menu__login').style.display = "inline";
    document.querySelector('div.menu__logout').style.display = "none";
  } else {
    document.querySelector('div.menu__login').style.display = "none";
    document.querySelector('div.menu__logout').style.display = "inline";
  }
});

$('.logout').click(function () {
  sessionStorage.clear();
  window.location.href = '/index.html'; 
});

$('.toMain').click(function(e) {
  sessionStorage.removeItem("page");
})

$('.toAbout').click(function(e) {
  sessionStorage.removeItem("page");
})

$('.toPlan').click(function(e) {
  var token = sessionStorage.getItem("token");
  sessionStorage.removeItem("page");   
  if ( !token ) {
  alert("로그인이 필요합니다");
  e.preventDefault();
  window.location.href = "/view/member/login.html";
  }
});

$('.toMyblog').click(function(e) {
  var token = sessionStorage.getItem("token");
  sessionStorage.removeItem("page");   
  if ( !token ) {
  alert("로그인이 필요합니다");
  e.preventDefault();
  window.location.href = "/view/member/login.html";
  }
})

$('.toStory').click(function(e) {
  sessionStorage.removeItem("page");
})