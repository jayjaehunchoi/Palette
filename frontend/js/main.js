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