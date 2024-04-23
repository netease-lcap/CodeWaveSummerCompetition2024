<template>
    <el-color-picker  @change="handleChange" v-model="currentValue"></el-color-picker>
</template>

<script>
import { MField } from '../../widgets/m-field';
import ColorPicker from "element-ui/lib/color-picker"
import Button from "element-ui/lib/button"
import Input from "element-ui/lib/button"
import Vue from "vue"
// import "element-ui/lib/theme-chalk/base.css"
import "element-ui/lib/theme-chalk/color-picker.css"
import "element-ui/lib/theme-chalk/button.css"
import "element-ui/lib/theme-chalk/input.css"
Vue.use(ColorPicker)
Vue.use(Button)
Vue.use(Input)

export default {
    name:"lxy-color-picker",
     mixins: [
      MField
    ],
    props:{
      value:{
        type:String,
      }
    },
    data(){
      return {
          currentValue:""
      }
    },
    watch:{
      value:{
        handler(value){
          this.currentValue = value
        },
        immediate:true
      }
    },
    methods:{
      handleChange(e){
        this.$emit('update:value', e)
        this.$emit('onChange',e)
        this.$emit('update', e)
      },
    }
}
</script>

<style>

</style>