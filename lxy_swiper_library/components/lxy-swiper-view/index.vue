<template>
   <swiper class="swiper lxy-swiper" :options="swiperOption">
    <swiper-slide v-for="(item,index) in value" :key="index">
      <img :data-src="item" class="swiper-lazy">
      <div class="swiper-lazy-preloader swiper-lazy-preloader-white"></div>
    </swiper-slide>
    <div class="swiper-pagination" slot="pagination"></div>
    <div class="swiper-button-next swiper-button-white" slot="button-next"></div>
    <div class="swiper-button-prev swiper-button-white" slot="button-prev"></div>
  </swiper>
</template>

<script>
import { Swiper, SwiperSlide } from 'vue-awesome-swiper'
import 'swiper/css/swiper.css'
export default {
    name:"lxy-swiper-view",
    components: {
      Swiper,
      SwiperSlide
    },
    props:{
      value:{
        type:Array,
        default:()=>[]
      }
    },
     data() {
      return {
       swiperOption: {
          lazy: true,
          pagination: {
            el: '.swiper-pagination',
            clickable: true
          },
          navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev'
          }
        }
      }
    },
}
</script>

<style>
.swiper-slide {
    text-align: center;
    background: #444 !important;
    position: relative;
}

.lxy-swiper{
  height: 480px;
  width: 100%;
}
</style>