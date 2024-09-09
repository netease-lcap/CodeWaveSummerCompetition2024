<template>
  <div ref="radarChart" :style="chartStyle"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "RadarChart",
  props: {
    //标题内容
    titleText: { type: String,default: 'Basic Radar Chart'},
    //标题是否显示
    titleShow: {type: Boolean,default: true},
    //标题垂直位置，可以是'top', 'middle', 'bottom' 或者具体的像素值如 '50px'
    titleVerticalPosition: {type: String,default: 'top'  },
    //标题水平位置 可以是 'left', 'center', 'right' 或者具体的百分比如 '50%'
    titleHorizontalPosition: {type: String,default: 'center' },
    //标题是否可以跳转
    titleLink: {type: Boolean,default: false},
    //跳转地址
    titleHref: {type: String,default: 'https://sf.163.com'},
    //链接打开方式 父窗口打开 'self' 或 新的窗口打开 'blank'。
    titletarget: {type: String,default: 'blank' },
    // 标题字体大小
    titleFontSize: {type: [String, Number],default: 16},
    //标题字体粗细
    //'bold', 'bolder', 'lighter', 'normal'或者100-900的数值
    fontWeight: {type: String,default: 'bold'},
    //标题字体风格
    //'normal', 'italic', 'oblique'
    fontStyle: {type: String,default: 'normal'},
    //标题字体颜色
    //十六进制色号
    titleFontColor: {type: String,default: '#333'},
    // 提示框显示配置
    tooltipShow: {type: Boolean,default: true,},
    tooltipBackgroundColor: {type: String,default: '#fff',},
    tooltipBorderColor: {type: String,default: '#333',},
    // 图例配置
    legendShow: {type: Boolean,default: true,},
    //图例类型，如 'plain'（普通图例）或 'scroll'（可滚动图例）
    legendType: {type: String,default: 'plain',},
    //图例组件的垂直布局位置，默认为 'bottom'
    legendTop: {type: String,default: 'bottom',},
    //图例组件的水平布局位置，默认为 'center'
    legendLeft: {type: String,default: 'center',},
    //图例布局朝向，默认为 'horizontal'，可选为 'vertical'
    legendOrient: {type: String,default: 'horizontal',},
    //雷达图的轮廓形状，如 'circle', 'polygon', 'star', 'ring', 'sector', 'cross', 'rect'
    radarShape: {type: String,default: 'polygon',},
    indicators: {
      type: Array,
      default: () => [
        { name: 'Sales', max: 6500 },
        { name: 'Administration', max: 16000 },
        { name: 'Information Technology', max: 30000 },
        { name: 'Customer Support', max: 38000 },
        { name: 'Development', max: 52000 },
        { name: 'Marketing', max: 25000 }
      ]
    },

    // 系列配置
    //系列拐点的图形，如 'circle', 'rect'
    seriesSymbol: {
      type: String,
      default: 'circle',
    },
    //系列拐点的大小
    seriesSymbolSize: {
      type: Number,
      default: 6,
    },
    dataSeries: {
      type: Array,
      default: () => ([
        {
          name: 'Allocated Budget',
          value: [4200, 3000, 20000, 35000, 50000, 18000]
        },
        {
          name: 'Actual Spending',
          value: [5000, 14000, 28000, 26000, 42000, 21000]
        }
      ])
    },
    chartStyle: {
      type: Object,
      default: () => ({
        height: '400px',
        width: '100%' // 设置为100%以实现宽度自适应
      })
    }
  },
  data() {
    return {
      chart: null // 存储 ECharts 实例
    };
  },
  watch: {
    indicators: {
      deep: true,
      handler(newVal) {
        this.updateChart();
      }
    },
    dataSeries: {
      deep: true,
      handler(newVal) {
        this.updateChart();
      }
    }
  },
  mounted() {
    this.initRadarChart();
    window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    if (this.chart !== null) {
      this.chart.dispose();
    }

    window.removeEventListener('resize', this.resizeChart);
  },
  methods: {
    initRadarChart() {
      this.chart = echarts.init(this.$refs.radarChart);
      this.updateChart();
    },
    updateChart() {
      if (!this.chart) return;

      const option = {
        title: {
          text: this.titleText,
          show: this.titleShow,
          top: this.titleVerticalPosition,
          left: this.titleHorizontalPosition,
          link: this.titleLink ? this.titleHref : null,
          target: this.titletarget,
          textStyle: {
          
            fontSize: this.titleFontSize,
            fontWeight: this.fontWeight,
            fontStyle: this.fontStyle,
            color: this.titleFontColor
          }
        },
        tooltip: {
          show: this.tooltipShow,
          backgroundColor: this.tooltipBackgroundColor,
          borderColor: this.tooltipBorderColor,
        },
        legend: {
          show: this.legendShow,
          type: this.legendType,
          top: this.legendTop,
          left: this.legendLeft,
          orient: this.legendOrient,
          data: this.dataSeries.map(series => series.name)
        },
        radar: {
          shape : this.radarShape,
          indicator: this.indicators,
          
        },
        //系列
        series: this.dataSeries.map((series, index) => ({
          name: series.name,
          type: 'radar',
          symbol: this.seriesSymbol,
          symbolSize: this.seriesSymbolSize, 
          itemStyle: {
            normal: {
              
            }
          },
          data: [
            {
              value: series.value,
              name: series.name
            }
          ]
        }))
      };

      this.chart.setOption(option);
    },
    resizeChart() {
      if (this.chart) {
        this.chart.resize();
      }
    }
  }
};
</script>

<style scoped>
/* 组件内的样式可以省略，因为样式已经通过 props 的方式传递 */
</style>
