<template>
  <LvColorpicker
    v-model="color"
    :label="label"
    :withoutInput="withoutInput"
    :hidePalette="hidePalette"
    :colors="colors"
    :clearable="clearable"
/>
</template>

<script>
import LvColorpicker from 'lightvue/color-picker';

export default {
  name:"color-picker",
  components: {
    LvColorpicker
  },
  props:{
    value:{ //默认色值
      type:String,
      default:"#000000"
    },
    label:{ //默认标题
      type:String,
      default:"颜色选择器"
    },
    colors: { // 默认调色板
      type: Array,
      default: () => [
          '#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5',
          '#2196F3', '#03A9F4', '#00BCD4', '#009688', '#4CAF50',
          '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107',
          '#FF9800', '#795548'
      ]
    },
    hidePalette: {//默认不隐藏调色板
      type:Boolean,
      default:false
    },
    withoutInput:{ //默认不隐藏输入框
      type:Boolean,
      default:false
    },
    clearable :{ //默认可清除输入框
      type:Boolean,
      default:true
    },
  
  },
  emits: ['change'],
  data() {
    return {
      color: this.value,
      oldColorValue: this.value  // 存储变更前的色值
    }
  },
  watch: {
    value: {
      handler(newValue, oldValue) {
        if (newValue !== this.color) {
          this.oldColorValue = this.color; // 更新变更前的色值
          this.color = newValue; // 更新当前色值
        }
      }
    },
    color: {
      handler(newValue, oldValue) {
        if (newValue !== oldValue) { // 只有当新值不等于旧值时才触发
          const changeEvent = {
            oldColorValue: this.oldColorValue, // 传递变更前的色值
            newColorValue: newValue // 传递变更后的色值
          };
          this.oldColorValue = newValue; // 更新oldColorValue为当前值
          this.$emit('change', changeEvent);
          this.$emit("update:value", newValue);
          //console.log(changeEvent);
        }
      }
    }
  }
}
</script>

<style>
.light-icon-x::before {
content: "×"; /* 使用乘号作为叉号 */
font-size: 20px; 
display: flex; /* 使用 Flex 布局来水平和垂直居中 */
justify-content: center; /* 水平居中 */
align-items: center; /* 垂直居中 */
}
</style>