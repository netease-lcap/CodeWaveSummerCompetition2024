<!--轮播图-->
<template>
  <div class="banner">
    <div class="slider-wrap">
      <ul
        class="slider-items"
        :style="[sliderActive, { width: sliderImg.length * 100 + 'vw' }]"
        @touchstart="stopSlider($event)"
        @touchmove="moveSlider($event)"
        @touchend="continSilder($event)"
      >
        <li
          class="slider-item"
          v-for="(item, index) in sliderImg"
          :key="`slider-item-${index}`"
          @click.stop="linkURl(item)"
        >
          <img :src="item.imgUrl" class="item-img" :style="item.imgCss" />
        </li>
      </ul>
    </div>
    <ul class="banner-page">
      <li
        class="slider-btn"
        v-for="(item, index) in bannerlist"
        :key="`slider-btn-${index}`"
        :class="{ active: index === nowSlider }"
        @click.stop="onClickPage(index)"
      ></li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "banner",
  props: {
    bannerlist: {
      require: true,
      // default: [
      //   {
      //     imgUrl:
      //       "https://community.codewave.163.com/upload/app/image_cb6c29124b9f47c5be3f9bf1e64a2570_afaec238-82f5-4996-8fdd-0aae42f393de_efp5NYD6_20240228093749015_ori.png",
      //     linkUrl:
      //       "https://community.codewave.163.com/upload/app/image_cb6c29124b9f47c5be3f9bf1e64a2570_afaec238-82f5-4996-8fdd-0aae42f393de_efp5NYD6_20240228093749015_ori.png",
      //   },
      //   {
      //     imgUrl:
      //       "https://community.codewave.163.com/upload/app/image_cb6c29124b9f47c5be3f9bf1e64a2570_afaec238-82f5-4996-8fdd-0aae42f393de_efp3MHZ9_20240227123642225_ori.png",
      //     linkUrl:
      //       "https://community.codewave.163.com/upload/app/image_cb6c29124b9f47c5be3f9bf1e64a2570_afaec238-82f5-4996-8fdd-0aae42f393de_efp3MHZ9_20240227123642225_ori.png",
      //   },
      // ],
    },
    customClickLink: {
      default: false,
      require: false,
    },
  },
  data() {
    return {
      sliderActive: {
        transform: "translate3d(-100vw,0,0)",
        transition: "transform 2s",
      },
      interTimer: "",
      startTouch: "",
      moveTouch: "",
      canslider: true,
      nowSlider: 0,
    };
  },
  computed: {
    sliderImg: function () {
      let saveImg = [];
      if (this.bannerlist && this.bannerlist.length > 0) {
        saveImg = JSON.parse(JSON.stringify(this.bannerlist));
        saveImg.unshift(this.bannerlist[this.bannerlist.length - 1]);
        saveImg.push(this.bannerlist[0]);
      }
      clearInterval(this.interTimer);
      this.setIntervalBanner();
      return saveImg;
    },
  },
  mounted() {},
  methods: {
    setIntervalBanner() {
      if (this.bannerlist && this.bannerlist.length > 0) {
        this.interTimer = setInterval(() => {
          this.sliderStart();
        }, 3000);
      }
    },
    linkURl(item) {
      this.$emit("click", item);
      this.$emit("update:value", val);
      if (!customClickLink) {
        // 使用自定义的点击事件
        if (item.linkUrl) {
          window.open(item.linkUrl);
        }
      }
    },
    onClickPage(index) {
      clearInterval(this.interTimer);
      this.nowSlider = index;
      this.sliderActive = Object.assign(
        {},
        {
          transition: "transform 0.5s",
          transform: `translate3d(${-100 * (index + 1)}vw,0,0)`,
        }
      );
      this.setIntervalBanner();
    },
    stopSlider(e) {
      if (e.target != e.currentTarget) {
        clearInterval(this.interTimer);
        delete this.sliderActive["transition"];
        this.startTouch = e.targetTouches[0].screenX;
      }
    },
    moveSlider(e) {
      if (this.nowSlider === -1 || this.nowSlider === this.bannerlist.length) {
        return;
      }
      if (e.target != e.currentTarget) {
        this.moveTouch = e.targetTouches[0].screenX;
        let slideDir = this.moveTouch - this.startTouch,
          targetWidth = parseInt(window.getComputedStyle(e.target).width),
          tranDir;
        if (slideDir < -50 || slideDir > 50) {
          if (slideDir < 0) {
            slideDir -= 50;
          } else {
            slideDir += 50;
          }
          tranDir = -targetWidth * (this.nowSlider + 1) + slideDir;
          this.sliderActive.transform = `translate3d(${tranDir}px,0,0)`;
        }
      }
    },
    continSilder(e) {
      if (e.target != e.currentTarget && this.moveTouch) {
        const slideDir = this.moveTouch - this.startTouch;
        if (slideDir < 0) {
          this.nowSlider++;
        } else if (slideDir > 0) {
          this.nowSlider--;
        }
        this.nowSlider--;
        this.sliderStart();
        this.moveTouch = 0;
        this.startTouch = 0;
        this.interTimer = setInterval(() => {
          this.sliderStart();
        }, 3000);
      }
    },
    sliderStart() {
      this.nowSlider++;
      this.nowSlider %= this.sliderImg.length;
      if (this.nowSlider === this.bannerlist.length) {
        setTimeout(() => {
          this.sliderActive = {
            transform: `translate3d(-100vw,0,0)`,
          };
          this.nowSlider = 0;
        }, 500);
      }
      if (this.nowSlider === -1) {
        setTimeout(() => {
          this.nowSlider = this.bannerlist.length - 1;
          this.sliderActive = {
            transform: `translate3d(${-100 * (this.nowSlider + 1)}vw,0,0)`,
          };
        }, 500);
      }
      this.sliderActive = Object.assign(
        {},
        {
          transition: "transform 0.5s",
          transform: `translate3d(${-100 * (this.nowSlider + 1)}vw,0,0)`,
        }
      );
    },
  },
};
</script>
<style scoped lang="scss">
.banner {
  height: 30vw;
  --custom-start: auto;
  border: var(--panel-border-width) solid var(--border-color-base);
  box-shadow: var(--panel-box-shadow);
  border-radius: var(--panel-border-radius);
  background: var(--panel-background);
  position: relative;
}
.banner-page {
  position: absolute;
  bottom: 1rem;
  justify-content: center;
  display: flex;
  align-items: center;
  width: 100%;
}
.slider-btn {
  width: 0.8rem;
  height: 0.8rem;
  background-color: #aaa;
  border-radius: 50%;
  margin-right: 0.5rem;
}
.slider-btn.active {
  background: #c62f2f;
}
.slider-wrap {
  height: 100%;
  overflow: hidden;
}
.slider-items {
  height: 100%;
  font-size: 0;
  overflow: hidden;
  display: flex;
  align-items: center;
}
.slider-item {
  width: 100vw;
  height: 100%;
  float: left;
}
.slider-item .item-img {
  width: 100%;
}
</style>
