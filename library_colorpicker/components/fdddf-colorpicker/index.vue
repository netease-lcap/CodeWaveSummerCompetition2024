<template>
  <LvColorpicker
      v-model="color"
      :label="label"
      :withoutInput="withoutInput"
      :hidePalette="hidePalette"
      :colors="colors"
      clearable
  />
</template>

<script>
import LvColorpicker from 'lightvue/color-picker';

export default {
    name:"fdddf-colorpicker",
    components: {
      LvColorpicker
    },
    props:{
      value:{
        type:String,
        default:"#000000"
      },
      colors: { // 默认的调色板
        type: Array,
        default: () => [
            '#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5',
            '#2196F3', '#03A9F4', '#00BCD4', '#009688', '#4CAF50',
            '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107',
            '#FF9800', '#795548'
        ]
      },
      hidePalette: {//是否显示调色板
        type:Boolean,
        default:false
      },
      label:{ //顶部文字
        type:String,
        default:"ColorPicker"
      },
      withoutInput:{ //没有输入框
        type:Boolean,
        default:false
      },
    },
    emits: ['change'],
    data() {
      return {
        color: this.value
      }
    },
    watch: {
      value(val) {
        this.color = val
        this.$emit('change', { val })
      }
    },
    mounted() {
      // console.log('mounted: ' + this.color)
    },
    methods: {
    },
}
</script>

<style>

</style>