<template>
  <div :style="{height:height+'px',width:width==0 ? '100%':width+'px'}">
    <vl-map
    :load-tiles-while-animating="true" 
    :load-tiles-while-interacting="true" 
    :zoom="zoom" 
    :center="center"
    :max-zoom="maxZoom"
    :min-zoom="minZoom"
    ref="map"
    >
      <vl-view :zoom.sync="zoom" :center.sync="center" :rotation.sync="rotation" />
      <vl-layer-tile>
        <vl-source-xyz :url.sync="url" :maxZoom.sync="maxZoom" :minZoom.sync="minZoom" :tileSize.sync="tileSize" />
      </vl-layer-tile>
    </vl-map>
  </div>

</template>

<script>
import Vue from 'vue'
import VueLayers from 'vuelayers'
import 'vuelayers/dist/vuelayers.css' // needs css-loader

Vue.use(VueLayers)

export default {
    name:"mapviewer",
    components:{
      VueLayers,
    },
    mounted(){
      // console.log(this.value)
    },
    props:{
      value:{
        type:String,
        default:"http://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}"
      },
      zoomLevel:{ //默认缩放等级
        type:Number,
        default:3
      },
      maxZoom:{ //最大缩放等级
        type:Number,
        default:18
      },
      minZoom:{ //最小缩放等级
        type:Number,
        default: 4
      },
      tileSize:{ //瓦片大小
        type:Number,
        default: 256
      },
      width: { //宽度
        type:Number,
        default: 0
      },
      height:{ //高度
        type:Number,
        default: 400
      }
    },
    data(){
      return{
        url: this.value,
        zoom: this.zoomLevel,
        rotation: 0,
        center: [0, 0]
      }
    },
    watch: {
      value(val) {
        this.url = val
        this.$refs.map.refresh()
        // console.log("watch url changed" + this.value)
      }
    }
}
</script>

<style>

</style>
