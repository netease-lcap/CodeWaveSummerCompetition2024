<template>
  <div>
    <!-- 地图容器 -->
    <l-map style="height: 500px" :zoom="zoom" :center=center :minZoom="minZoom" :maxZoom="maxZoom" :maxBoundsViscosity="maxBoundsViscosity" :crs="CRS.Simple">
      <!-- 图片瓦片图层 -->
      <l-tile-layer :url="pictureURL" :maxZoom="maxZoom" :tileSize="tileSize" :noWrap="true" :attribution="attribution"></l-tile-layer>
    </l-map>
  </div>
</template>

<script>
import {LMap, LTileLayer} from 'vue2-leaflet';
import {CRS} from 'leaflet'

export default {
  name:"tiled-pic-loader",
  computed: {
    CRS() {
      return CRS
    }
  },
  components: {LMap, LTileLayer},
  data () {
    return {
      maxBoundsViscosity: 1.0,
      center: [0, 0]
    };
  },
  props:{
    // 图片URL
    pictureURL:{
      type: String,
      default: 'https://example.com/pic_test/{z}-{x}-{y}.png'
    },
    // 瓦片大小
    tileSize: {
      type: Number,
      default: 512
    },
    // 最大缩放级别
    maxZoom: {
      type: Number,
      default: 3
    },
    // 版权信息
    attribution: {
      type: String,
      default: '大图瓦片加载'
    }
  },
  mounted() {
    // 加载 Leaflet CSS 样式表
    var link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = 'https://unpkg.com/leaflet@1.6.0/dist/leaflet.css';
    document.head.appendChild(link);
    this.center.fill(200, 0, 1);
    this.center.fill(0, 1, 2);
  },
  watch: {
    pictureURL(newValue, oldValue){
      this.url = newValue;
    },
    tileSize(newValue, oldValue){
      this.tileSize = newValue;
    },
    maxZoom(newValue, oldValue){
      this.maxZoom = newValue;
    },
    attribution(newValue, oldValue){
      this.attribution = newValue;
    }
  },
  methods: {
  }
}
</script>

<style>

</style>