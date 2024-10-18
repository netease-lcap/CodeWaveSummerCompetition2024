<template>
  <div class="relation-graph-container">
    <RelationGraph v-if="!isIDE && !isFailed" ref="graphRef" :options="graphOptions" />
    <div v-if="!isIDE && isFailed" class="error-container">
      <div class="error-animation">
        <!-- 这里可以添加一个加载失败的动画，比如使用 lottie-web 或其他动画库 -->
        <i class="error-icon">❌加载失败</i>
      </div>
    </div>
  </div>
</template>

<script>
import RelationGraph from 'relation-graph'

export default {
  name: 'relation-graph',
  components: { RelationGraph },
  props: {
    //根节点id
    rootId: { type: String, },
    //节点数据
    nodes: { type: Array, default: () => [] },
    //关系线条数据
    lines: { type: Array, default: () => [] },
    //布局方式（tree树状布局/center中心布局/force自动布局）
    layout: { type: String, default: 'center' },
    //left:从左到右/top:从上到下/right:从右到左/bottom:从下到上 
    layoutFrom: { type: String, default: 'top' },
    //设置默认的连接点位置。通常是 'border'，表示线条连接到节点的边界。
    defaultJunctionPoint: { type: String, default: 'border' },
    //设置图表的背景颜色。
    backgroundColor: { type: String, default: '#fff' },
    //图谱水印url,如：https://ssl.relation-graph.com/images/relatioon-graph-canvas-bg.png
    backgroundImage: { type: String, default: null },
    //只在右下角显示水印，不重复显示水印
    backgroundImageNoRepeat: { type: Boolean, default: false },
    //是否显示工具栏
    allowShowMiniToolBar: { type: Boolean, default: true },
    //是否在工具栏中显示【自动布局】按钮
    allowAutoLayoutIfSupport: { type: Boolean, default: true },
    //是否在工具栏中显示【刷新】按钮
    allowShowRefreshButton: { type: Boolean, default: true },
    //是否在工具栏中显示【下载】按钮
    allowShowDownloadButton: { type: Boolean, default: true },
    //下载图片的文件名
    downloadImageFileName: { type: String, default: '关系图' },
    //是否禁用缩放
    disableZoom: { type: Boolean, default: false },
    //是否禁用节点拖拽
    disableDragNode: { type: Boolean, default: false },
    //是否在右侧菜单栏显示放大缩小的按钮
    allowShowZoomMenu: { type: Boolean, default: true },
    //是否禁用节点默认的点击效果（选中、闪烁）
    disableNodeClickEffect: { type: Boolean, default: true },
    //是否禁用线条默认的点击效果（选中、闪烁）
    disableLineClickEffect: { type: Boolean, default: true },
  },
  data() {
    return {
      graphOptions: {
        // 初始化图表选项
        defaultJunctionPoint: this.defaultJunctionPoint,
        backgroundColor: this.backgroundColor,
        backgroundImage: this.backgroundImage,
        backgroundImageNoRepeat: this.backgroundImageNoRepeat,
        allowShowMiniToolBar: this.allowShowMiniToolBar,
        allowShowDownloadButton: this.allowShowDownloadButton,
        downloadImageFileName: this.downloadImageFileName,
        disableNodeClickEffect: this.disableNodeClickEffect,
        disableLineClickEffect: this.disableLineClickEffect,
        disableZoom: this.disableZoom,
        disableDragNode: this.disableDragNode,
        allowShowZoomMenu: this.allowShowZoomMenu,
        allowAutoLayoutIfSupport: this.allowAutoLayoutIfSupport,
        allowShowRefreshButton: this.allowShowRefreshButton,
        layout: {
          //布局方式（tree树状布局/center中心布局/force自动布局）
          layoutName: this.layout,
          //left:从左到右/top:从上到下/right:从右到左/bottom:从下到上
          from: this.layoutFrom,
        }
      },
      isFailed: false,
    }
  },
  computed: {
    isIDE() {
      return this.$env && this.$env.VUE_APP_DESIGNER;
    },
  },
  mounted() {
    this.initGraph()
  },
  methods: {
    initGraph() {
      if (this.isIDE) return;

      this.isFailed = false;

      if (!Array.isArray(this.nodes) || this.nodes.length === 0) {
        this.setError('节点数据缺失或为空');
        return;
      }

      if (!Array.isArray(this.lines)) {
        this.setError('关系线数据不是数组');
        return;
      }

      if (!this.rootId) {
        this.setError('根节点ID未设置');
        return;
      }

      const rootNode = this.nodes.find(node => node.id === this.rootId);
      if (!rootNode) {
        this.setError(`在节点数据中未找到ID为"${this.rootId}"的根节点`);
        return;
      }

      // 组合数据
      const jsonData = {
        rootId: this.rootId,
        nodes: this.nodes,
        lines: this.lines
      };

      // 以上数据中的node和link可以参考"Node节点"和"Link关系"中的参数进行配置
      this.$refs.graphRef.setJsonData(jsonData, (graphInstance) => {
        // Called when the relation-graph is completed
      })
    },
    // 重新加载图谱
    reload() {
      this.initGraph();
    },
    setError(message) {
      console.error(`加载失败：${message}`);
      this.isFailed = true;
    },
  }
}
</script>

<style scoped>
.relation-graph-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  border: 1px dashed #ccc;
}

.error-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.error-animation {
  font-size: 20px;
  margin-bottom: 20px;
}

.error-message {
  color: red;
  text-align: center;
}
</style>
