<template>
  <div id="xmind-container"  :class="isIDE ? 'ide-style' : ''">
    <img v-if="showLoading" src="https://assets.xmind.cn/www/assets/images/share/xmind-b6f6b3ca68.svg" class="xmind-logo" >
    <div v-if="showLoading" class="loading"></div>
  </div>
</template>

<script>
export default {
  props: {
    fileUrl: {
      type: String,
    }
  },
  data() {
    return {
      showLoading: true,
      viewer: null
    }
  },
  computed: {
    isIDE() {
      return this.$env && this.$env.VUE_APP_DESIGNER;
    },
  },
  watch: {
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
     
      this.showLoading = true;

      if (this.isIDE) return;

      // 如果实例已存在，仅加载新的文件
      if (this.viewer) {
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
      console.error('加载xmind文件出错:', err)
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
  position: relative;
}

.ide-style {
  border: 1px dashed #ccc;
  height: 300px !important;
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
.xmind-logo {
  position: absolute;
  top: 14px;
  left: 14px;
  height: auto;
}
</style>

