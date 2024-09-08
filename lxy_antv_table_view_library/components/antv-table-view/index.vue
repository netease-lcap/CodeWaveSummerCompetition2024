<template>
  <div id="container"></div>
</template>

<script>
import { S2Event, PivotSheet, TableSheet } from "@antv/s2";
const defaultData = {
  "describe": "标准交叉表数据。",
  "fields": {
    "rows": ["province", "city","area"],
    "columns": ["type", "sub_type"],
    "values": ["number"],
    "valueInCols": true
  },
  "meta": [
    {
      "field": "number",
      "name": "数量"
    },
    {
      "field": "province",
      "name": "省份"
    },
    {
      "field": "city",
      "name": "城市"
    },
    {
      "field": "area",
      "name": "镇"
    },
    {
      "field": "type",
      "name": "类别"
    },
    {
      "field": "sub_type",
      "name": "子类别"
    }
  ],
  "data": [
    {
      "number": 7789,
      "province": "浙江省",
      "city": "杭州市",
      "area": "陵铺",
      "type": "家具",
      "sub_type": "桌子"
    },
      {
      "number": 7789,
      "province": "浙江省",
      "city": "杭州市",
      "area": "陵铺1",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 2367,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 3877,
      "province": "浙江省",
      "city": "宁波市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 4342,
      "province": "浙江省",
      "city": "舟山市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 5343,
      "province": "浙江省",
      "city": "杭州市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 632,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 7234,
      "province": "浙江省",
      "city": "宁波市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 834,
      "province": "浙江省",
      "city": "舟山市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 945,
      "province": "浙江省",
      "city": "杭州市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 1304,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 1145,
      "province": "浙江省",
      "city": "宁波市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 1432,
      "province": "浙江省",
      "city": "舟山市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 1343,
      "province": "浙江省",
      "city": "杭州市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 1354,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 1523,
      "province": "浙江省",
      "city": "宁波市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 1634,
      "province": "浙江省",
      "city": "舟山市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 1723,
      "province": "四川省",
      "city": "成都市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 1822,
      "province": "四川省",
      "city": "绵阳市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 1943,
      "province": "四川省",
      "city": "南充市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 2330,
      "province": "四川省",
      "city": "乐山市",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 2451,
      "province": "四川省",
      "city": "成都市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 2244,
      "province": "四川省",
      "city": "绵阳市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 2333,
      "province": "四川省",
      "city": "南充市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 2445,
      "province": "四川省",
      "city": "乐山市",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 2335,
      "province": "四川省",
      "city": "成都市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 245,
      "province": "四川省",
      "city": "绵阳市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 2457,
      "province": "四川省",
      "city": "南充市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 2458,
      "province": "四川省",
      "city": "乐山市",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 4004,
      "province": "四川省",
      "city": "成都市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 3077,
      "province": "四川省",
      "city": "绵阳市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 3551,
      "province": "四川省",
      "city": "南充市",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 352,
      "province": "四川省",
      "city": "乐山市",
      "type": "办公用品",
      "sub_type": "纸张"
    }
  ],
  "totalData": [
    {
      "number": 26193,
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 49709,
      "type": "家具"
    },
    {
      "number": 23516,
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 29159,
      "type": "办公用品"
    },
    {
      "number": 12321,
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 16838,
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 18375,
      "province": "浙江省",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 14043,
      "province": "浙江省",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 4826,
      "province": "浙江省",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 5854,
      "province": "浙江省",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 7818,
      "province": "四川省",
      "type": "家具",
      "sub_type": "桌子"
    },
    {
      "number": 9473,
      "province": "四川省",
      "type": "家具",
      "sub_type": "沙发"
    },
    {
      "number": 7495,
      "province": "四川省",
      "type": "办公用品",
      "sub_type": "笔"
    },
    {
      "number": 10984,
      "province": "四川省",
      "type": "办公用品",
      "sub_type": "纸张"
    },
    {
      "number": 13132,
      "province": "浙江省",
      "city": "杭州市",
      "type": "家具"
    },
    {
      "number": 2288,
      "province": "浙江省",
      "city": "杭州市",
      "type": "办公用品"
    },
    {
      "number": 15420,
      "province": "浙江省",
      "city": "杭州市"
    },
    {
      "number": 2999,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "家具"
    },
    {
      "number": 2658,
      "province": "浙江省",
      "city": "绍兴市",
      "type": "办公用品"
    },
    {
      "number": 5657,
      "province": "浙江省",
      "city": "绍兴市"
    },
    {
      "number": 11111,
      "province": "浙江省",
      "city": "宁波市",
      "type": "家具"
    },
    {
      "number": 2668,
      "province": "浙江省",
      "city": "宁波市",
      "type": "办公用品"
    },
    {
      "number": 13779,
      "province": "浙江省",
      "city": "宁波市"
    },
    {
      "number": 5176,
      "province": "浙江省",
      "city": "舟山市",
      "type": "家具"
    },
    {
      "number": 3066,
      "province": "浙江省",
      "city": "舟山市",
      "type": "办公用品"
    },
    {
      "number": 8242,
      "province": "浙江省",
      "city": "舟山市"
    },
    {
      "number": 4174,
      "province": "四川省",
      "city": "成都市",
      "type": "家具"
    },
    {
      "number": 6339,
      "province": "四川省",
      "city": "成都市",
      "type": "办公用品"
    },
    {
      "number": 10513,
      "province": "四川省",
      "city": "成都市"
    },
    {
      "number": 4066,
      "province": "四川省",
      "city": "绵阳市",
      "type": "家具"
    },
    {
      "number": 3322,
      "province": "四川省",
      "city": "绵阳市",
      "type": "办公用品"
    },
    {
      "number": 7388,
      "province": "四川省",
      "city": "绵阳市"
    },
    {
      "number": 4276,
      "province": "四川省",
      "city": "南充市",
      "type": "家具"
    },
    {
      "number": 6008,
      "province": "四川省",
      "city": "南充市",
      "type": "办公用品"
    },
    {
      "number": 10284,
      "province": "四川省",
      "city": "南充市"
    },
    {
      "number": 4775,
      "province": "四川省",
      "city": "乐山市",
      "type": "家具"
    },
    {
      "number": 2810,
      "province": "四川省",
      "city": "乐山市",
      "type": "办公用品"
    },
    {
      "number": 7585,
      "province": "四川省",
      "city": "乐山市"
    },
    {
      "number": 32418,
      "province": "浙江省",
      "type": "家具"
    },
    {
      "number": 10680,
      "province": "浙江省",
      "type": "办公用品"
    },
    {
      "number": 43098,
      "province": "浙江省"
    },
    {
      "number": 17291,
      "province": "四川省",
      "type": "家具"
    },
    {
      "number": 18479,
      "province": "四川省",
      "type": "办公用品"
    },
    {
      "number": 35770,
      "province": "四川省"
    },
    {
      "number": 78868
    }
  ]
}

export default {
  name: "antv-table-view",
  props: {
    dataSource: {
      type: Array,
      default: () => [],
    },
    hierarchyType: {
      type: String,
      default: "grid",
    },
    fields:{
      type: Object,
      default: () => {},
    },
    meta: {
      type: Array,
      default: () => [],
    },
    width: {
      type: Number,
    },
    height:{
      type: Number
    },
    cellWidth:{
      type: Number
    },
    cellHeight:{
      type: Number,
    }
  },
  mounted() {
    if (this.dataSource) {
      this.bootstrap();
    }
  },
  watch: {
    hierarchyType: {
      handler(value) {
        this.s2.destroy();
        this.bootstrap();
      },
    },
    dataSource: {
      handler(value) {
        this.s2&& this.s2.destroy();
        this.bootstrap();
      },
      deep: true,
    },
    width:{
      handler(value) {
        this.s2&&this.s2.destroy();
        this.bootstrap();
      },
    },
    fields:{
      handler(value) {
        this.s2&&this.s2.destroy();
        this.bootstrap();
      },
    }
  },
  methods: {
    async bootstrap() {
      const s2Options = {
        width: this.width || 1000,
        height: this.height || 480,
        hierarchyType: this.hierarchyType,
        // showDefaultHeaderActionIcon: true,
        interaction: {
          copy: { enable: true },
          withFormat: true,
          withHeader: true,
          selectedCellsSpotlight: false, // 选中高亮
          hoverHighlight: false, // hover高亮
        },
        // frozenFirstRow:false,
        // showSeriesNumber:true,
        // 显示序号
        seriesNumber: {
          // enable: true,
          // 自定义序号列文本, 默认 "序号"
          // text: '自定义序号标题',
        },
        style: {
          layoutWidthType: "colAdaptive",
          // showTreeLeafNodeAlignDot:true,
          colCfg: {
            // hideMeasureColumn: true,
          },
          cellCfg: {
            width:this.cellWidth,
            height: this.cellHeight
          },
        },
        totals: {
          row: {
            // showSubTotals: true,
            // showGrandTotals: true,
            // reverseLayout: true,
          },
        },
        // frozen: {
          // 默认冻结行头, 行头和数值区域都会展示滚动条
          // rowHeader: false,
          // 冻结行头时, 行头宽度占表格的 1/2, 支持动态调整 (0 - 1)
          // rowHeader: 0.2,
        // },
      };

      const s2DataConfig = {
        ...this.defaultData,
        meta: this.meta,
        fields:this.fields,
        data:this.dataSource,
      };

      const container = document.getElementById("container");
      const s2 = new PivotSheet(container, s2DataConfig, s2Options);
      s2.setThemeCfg({ name: "gray" });
      this.s2 = s2;

      // const button = document.createElement('button');
      // const dataCellTooltip = () => {
      //   const button = document.createElement("button")
      //   button.innerText = '点击合并单元格';
      //   button.className = 'merge-cells-button';
      //   button.onclick = () => s2.interaction.mergeCells(); // 不传入 cellsInfo 时，默认使用当前选中所有的单元格信息
      //   return button;
      // };
      s2.on(S2Event.DATA_CELL_CLICK, (event,a) => {
        console.log(event);
        // s2.tooltip.show({
        //   position: { x: event.clientX, y: event.clientY },
        //   content: dataCellTooltip(),
        // });
      });
      //  s2.setTheme({
      //   rowCell: {
      //     cell: {
      //       backgroundColor: "red"
      //     }
      //   }
      // });
      await s2.render();

      // await this.s2.setOptions({...this.s2.options, hierarchyType: "tree"},false)
    },
  },
};
</script>

<style></style>
