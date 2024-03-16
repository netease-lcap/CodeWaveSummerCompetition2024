<template>
  <ColorPicker
    v-model="isShowColorPicker"
    type="linear"
    @changeColor="changeColor"
    @onClose="onClosePicker"
    :pDeg="90"
    :pColor="value"
    :showClose="isShowClose"
    :closeOnClickBody="false"
    :titleConfig="{ show: this.isShowTitle, text: this.title }"
  />
</template>

<script>
import ColorPicker from 'vue2-color-picker-gradient'

export default {
    name:"fdddf-colorpicker",
    components: {
      ColorPicker
    },
    props:{
      value:{
        type:String,
        default:"#000000" //默认颜色, {hex: '#000000',rgba: { r: 0, g: 0, b: 0, a: 1 },color: 'rgba(0,0,0,1)'}
      },
      isShowColorPicker: {//是否显示
        type:Boolean,
        default:true
      },
      title:{ //顶部文字
        type:String,
        default:"颜色选择器"
      },
      isShowClose:{ //是否显示
        type:Boolean,
        default:true
      },
      isShowTitle:{ //是否显示标题
        type:Boolean,
        default:true
      }
    },
    data() {
      return {
        style: '',
        titleConfig: {
          show: this.isShowTitle,//控制顶部文字 关闭按钮 显隐
          text: this.title//顶部文字
        }
      }
    },
    mounted() {
      this.run()
    },
    methods: {
      async run() {
      },
      changeColor({ style, colors, deg, color }) {
        this.value = color.hex
        this.$emit('change', { color })
        this.$emit("update:value", color.hex)
      },
      showPicker() {
        this.isShowColorPicker = true
      },
      onClosePicker() {
        this.isShowColorPicker = false
        this.$emit('close', {})
      },
    },
}
</script>

<style>

</style>