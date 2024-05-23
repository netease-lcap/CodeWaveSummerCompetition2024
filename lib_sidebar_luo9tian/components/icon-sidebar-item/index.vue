<template>
<!--  <div>{{value}}</div>-->
  <div class="icon-sidebar-item">
    <img v-show="iconUrl" :src="iconUrl" style="width: 15px;height: 15px;border-radius: 50%;">
    <a class="a-item" :href="linkAddress||'#'">{{text}}</a>
  </div>
</template>

<script>
export default {
    name:"icon-sidebar-item",
    props:{
      text:{
        type:String,
        default:"侧边栏文本"
      },
      linkAddress:{
        type: String,
        default: ""
      },
      value:{
        type:String,
      },
      iconUrl:{
        type:String,
        default:""
      }
    },
}
</script>

<style>

</style>