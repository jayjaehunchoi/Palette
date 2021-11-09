/**
 * 슬라이드 요소 관리
 */
// new Swiper(요소, 옵션);
new Swiper('.swiper-container', {
  direction: 'horizontal', // 수평 슬라이드
  scrollbarHide: true,
  slidesPerView: 3,
  spaceBetween: 300, // 슬라이드 사이 여백
  loop: true, // 반복 재생 여부
  grabCursor: true,
  freeModeSticky: true,
  centeredSlides: true,
  autoplay: { // 자동 재생 여부
  delay: 5000 // 5초마다 슬라이드 바뀜
  } 
});

// 범위 랜덤 함수(소수점 2자리까지)
function random(min, max) {
  // `.toFixed()`를 통해 반환된 문자 데이터를,
  // `parseFloat()`을 통해 소수점을 가지는 숫자 데이터로 변환
  return parseFloat((Math.random() * (max - min) + min).toFixed(2))
}
function figureObject(selector, delay, size) {
  // gsap.to(요소, 시간, 옵션);
  gsap.to(
    selector,  // 선택자
    random(1.5, 2.5),   // 애니메이션 동작 시간
    {  // 옵션
      y: size,
      repeat: -1, // 무한 반복
      yoyo: true,
      ease: Power1.easeInOut,
      delay: random(0, delay)
    }
  );
}
figureObject('.figure_triangle', 1, 15);
figureObject('.figure_square', .5, 15);


/**
 * 요소가 화면에 보여짐 여부에 따른 요소 관리
 */
// 관리할 요소들 검색!
const spyEls = document.querySelectorAll('section.scroll-spy');
// 요소들 반복 처리!
spyEls.forEach(function (spyEl) {
  new ScrollMagic
    .Scene({ // 감시할 장면(Scene)을 추가
      triggerElement: spyEl, // 보여짐 여부를 감시할 요소를 지정
      triggerHook: .8 // 화면의 80% 지점에서 보여짐 여부 감시
    }) 
    .setClassToggle(spyEl, 'show') // 요소가 화면에 보이면 show 클래스 추가
    .addTo(new ScrollMagic.Controller()); // 컨트롤러에 장면을 할당(필수!)
});