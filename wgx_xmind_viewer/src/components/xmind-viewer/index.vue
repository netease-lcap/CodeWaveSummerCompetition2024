<template>
  <div id="xmind-container">
  </div>
</template>

<script>
export default {
  name: "xmind-online-viewer",
  props: {
    fileUrl: {
      type: String,
      default: null,
    }
  },
  data() {
    return {
      viewer: null
    };
  },
  watch: {
    // 监听 url 属性
    fileUrl: {
      // 确保在初始化时也执行一次
      immediate: true, 
      handler: function(newUrl) {
        // 当 url 属性发生变化时，重新加载 xmind 文件
        this.loadXMindFile(newUrl);
      }
    }
  },
  methods: {
    initViewer() {
      import('xmind-embed-viewer').then(XMindEmbedViewer => {
        this.viewer = new XMindEmbedViewer.XMindEmbedViewer({
          el: '#xmind-container',
          region: 'cn'
        });
        this.viewer.setStyles({
          width: '100%',
          height: '100%'
        });
      }).catch(err => {
        console.error('导入xmind-embed-viewer失败！', err);
      });
    },
    async loadXMindFile(url) {
      try {
        const response = await fetch(url);
        const arrayBuffer = await response.arrayBuffer();
        this.viewer.load(arrayBuffer);
      } catch (err) {
        console.error('加载xmind文件出错：', err);
      } 
      // 由于不再显示加载动画，以下代码可以移除或注释掉
      // this.showLoading = false; // 文件加载完成后隐藏加载动画
    }
  },
  mounted() {
    // 初始化 xmind 插件
    this.initViewer();
  }
}
</script>

<style>
#xmind-container {
  position: relative; /* 使容器成为一个相对定位的元素 */
  width: 100%; /* 或者设置为特定宽度 */
  height: 100%; /* 或者设置为特定高度 */
  display: flex; /* 使用 flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

</style>