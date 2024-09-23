<template>
  <div id="xmind-container">
    <div v-if="showLoading" class="loading"></div>
  </div>
</template>

<script>
export default {
  props: {
    fileUrl: {
      type: String,
      //default: 'http://localhost:8008/中心主题.xmind',
    }
  },
  data() {
    return {
      showLoading: true,
      viewer: null
    }
  },
  watch: {
    // 监听 url 属性的变化
    fileUrl: {
      immediate: false,
      handler: 'loadXMind'
    }
  },
  mounted() {
    this.loadXMind()
  },
  methods: {
    async loadXMind() {
      // 显示加载动画
      this.showLoading = true;
      // 判断是否在IDE中运行
      if(process.env.VUE_APP_DESIGNER){
          this.fileUrl = '';
      }
      if (this.viewer) {
        // 如果实例已存在，仅加载新的文件
        try {
          const response = await fetch(this.fileUrl);
          const file = await response.arrayBuffer();
          this.viewer.load(file);
        } catch (err) {
          this.onError(err);
        }
      } else {
        // 如果实例不存在，创建新实例并加载文件
        try {
          const { XMindEmbedViewer } = await import('xmind-embed-viewer');
          this.viewer = new XMindEmbedViewer({
            el: '#xmind-container',
            region: 'cn'
          });
          this.viewer.setStyles({
            width: '100%',
            height: '100%'
          });
          this.viewer.addEventListener('map-ready', this.onMapReady);
          this.viewer.addEventListener('error', this.onError);

          const response = await fetch(this.fileUrl);
          const file = await response.arrayBuffer();
          this.viewer.load(file);
        } catch (err) {
          this.onError(err);
        }
      }
    },
    onMapReady() {
      this.showLoading = false
    },
    onError(err) {
      this.showLoading = false
      console.error('加载xmind文件出错！', err)
    }
  },
}
</script>

<style scoped>
#xmind-container {
  display: flex;
  height: 100%;
  width: 100%;
  align-items: center;
  justify-content: center;
}

.loading {
  display: block;
  position: absolute;
  width: 6px;
  height: 10px;
  animation: rectangle infinite 1s ease-in-out -0.2s;
  background-color: #000;
}

.loading:before,
.loading:after {
  position: absolute;
  width: 6px;
  height: 10px;
  content: "";
  background-color: #000;
}

.loading:before {
  left: -14px;
  animation: rectangle infinite 1s ease-in-out -0.4s;
}

.loading:after {
  right: -14px;
  animation: rectangle infinite 1s ease-in-out;
}

@keyframes rectangle {

  0%,
  80%,
  100% {
    height: 20px;
    box-shadow: 0 0 #000;
  }

  40% {
    height: 30px;
    box-shadow: 0 -20px #000;
  }
}
</style>