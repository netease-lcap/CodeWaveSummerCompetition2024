<template>
  <div >
    <carousel-3d
     :count="localImages.length"
     :disable3d="disable3d" 
     :controls-visible="controlsVisible" 
     :clickable="clickable" 
     :autoplay="autoplay" 
     :autoplay-timeout="autoplayTimeout" 
     :display="display"
     :width="width"
     :height="height"
     :perspective="perspective"
     @after-slide-change="onAfterSlideChange" 
     @before-slide-change="onBeforeSlideChange" 
     >
      <slide v-for="(img, i) in localImages" :index="i" :key="img">
        <figure>
          <img :src="img">
          <figcaption v-if="localTitles[i]">
            {{ localTitles[i] }}
          </figcaption>
        </figure>
      </slide>
    </carousel-3d>

  </div>
</template>

<script>

import { Carousel3d, Slide } from "vue-carousel-3d";

export default {
    name:"carousel",
    components: {
      Carousel3d,
      Slide,
    },
    props:{
      width:{
        type: Number,
        default: 360
      },
      height:{
        type: Number,
        default: 270
      },
      disable3d: {
        type: Boolean,
        default: false
      },
      controlsVisible: {
        type: Boolean,
        default: true
      },
      clickable: {
        type: Boolean,
        default: true
      },
      autoplay: {
        type: Boolean,
        default: true
      },
      autoplayTimeout: {
        type: Number,
        default: 5000
      },
      display: {
        type: Number,
        default: 3
      },
      perspective: {
        type: Number,
        default: 30,
      },
      images: {
        type: Array,
        require: true,
        default: [
          'https://placebeard.it/640/480',
          'https://placebeard.it/640/480',
          'https://placebeard.it/640/480',
        ],
      },
      titles: {
        type: Array,
        default: () => [
          'this is the first image',
          'this is the second image',
          'this is the third image',
        ],
      }
    },
    
    data() {
      return {
        localImages: this.images,
        localTitles: this.titles,
      }
    },
    watch: {
      images: function (newVal) {
        this.localImages = newVal;
        // console.log('images changed');
        // console.log(this.localImages);
        this.localImages
      },
      titles: function (newVal) {
        this.localTitles = newVal;
        // console.log('titles changed');
        // console.log(this.localTitles);
      }
    },
    methods: {
      onAfterSlideChange: function (index) {
        // console.log('after slide change', index);
        this.$emit('afterSlideChange', index);
      },
      onBeforeSlideChange: function (index) {
        // console.log('before slide change', index);
        this.$emit('beforeSlideChange', index);
      }
    }
}
</script>

<style>
.carousel-3d-container figure {
  margin:0;
}

.carousel-3d-container figcaption {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
  bottom: 0;
  position: absolute;
  bottom: 0;
  padding: 15px;
  font-size: 12px;
  min-width: 100%;
  box-sizing: border-box;
}
</style>