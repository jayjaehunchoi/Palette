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