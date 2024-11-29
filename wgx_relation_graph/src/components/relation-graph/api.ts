///<reference types="@nasl/types" />
namespace extensions.wgx_relation_graph.viewComponents {
  const { Component, Prop, ViewComponent, Slot, Method, Event, ViewComponentOptions } = nasl.ui;

  @ExtensionComponent({
    type: 'pc',
    ideusage: {
      idetype: 'element',
    }
  })
  @Component({
    title: '关系图',
    description: '关系图',
  })
  export class RelationGraph extends ViewComponent {

    @Method({
      title: '重新加载',
      description: '重新加载关系图数据',
    })
    reload(): void {}

    constructor(options?: Partial<RelationGraphOptions>) {
      super();
    }
  }

  export class RelationGraphOptions extends ViewComponentOptions {

    @Prop({
      group: '数据属性',
      title: '根节点',
      description: '根节点ID',
      setter: {
        concept: 'InputSetter'
      }
    })
    rootId: nasl.core.String;
    
    @Prop({
      group: '数据属性',
      title: '节点数据',
      docDescription: '节点数组，每个节点包含id、文本、背景颜色、字体颜色、边框宽度、边框颜色、形状、宽度和高度等信息。',
      setter: {
        concept: 'InputSetter'
      },
    })
    nodes: nasl.collection.List <any> | nasl.collection.List<{
      id: nasl.core.String,
      text: nasl.core.String,
      color: nasl.core.String,
      fontColor: nasl.core.String,
      borderWidth: nasl.core.Integer,
      borderColor: nasl.core.String,
      nodeShape: nasl.core.Integer,
      width: nasl.core.Integer,
      height: nasl.core.Integer,
      opacity: nasl.core.Decimal,
      isHide: nasl.core.Boolean,
      disableDrag: nasl.core.Boolean,
      disableDefaultClickEffect: nasl.core.Boolean
    }>; ;

    @Prop({
      group: '数据属性',
      title: '关系线数据',
      docDescription: '关系线数组，每条线包含起始节点、结束节点、文本、颜色、线宽、形状等信息。',
      setter: {
        concept: 'InputSetter'
      },
    })
    lines: nasl.collection.List <any> | nasl.collection.List<{
      from: nasl.core.String,
      to: nasl.core.String,
      text: nasl.core.String,
      color: nasl.core.String,
      lineWidth: nasl.core.Integer,
      isHide: nasl.core.Boolean,
      lineShape: nasl.core.Integer,
      fontColor: nasl.core.String,
      opacity: nasl.core.Decimal,
      isHideArrow: nasl.core.Boolean,
      useTextPath: nasl.core.Boolean
    }>;

    @Prop({
      group: '主要属性',
      title: '默认连接点位置',
      description: '设置默认的连接点位置',
      setter: {
        concept: 'EnumSelectSetter',
        options: [
          { title: '边界',},
          { title: '上下左右',},
          { title: '上下', },
          { title: '左右',},
        ]
      }
    })
    defaultJunctionPoint: 'border' | 'ltrb' | 'tb' | 'lr' = 'border';

    @Prop({
      group: '主要属性',
      title: '布局方式',
      description: '布局方式',
      setter: {
        concept: 'EnumSelectSetter',
        options: [
          { title: '树状布局',},
          { title: '中心布局',},
          { title: '自动布局',},
        ]
      }
    })
    layout: 'tree' | 'center' | 'force' = 'center';

    @Prop({
      group: '主要属性',
      title: '树状图布局方向',
      description: '树状图布局方向，只有当布局方式为树状图时有效',
      setter: {
        concept: 'EnumSelectSetter',
        options: [
          { title: '从左到右',},
          { title: '从上到下',},
          { title: '从右到左',},
          { title: '从下到上',},
        ]
      }
    })
    layoutFrom: 'left' | 'top' | 'right' | 'bottom' = 'top';

    @Prop({
      group: '样式属性',
      title: '背景颜色',
      description: '设置图表的背景颜色',
      setter: {
        concept: 'InputSetter'
      }
    })
    backgroundColor: nasl.core.String = '#fff';

    @Prop({
      group: '样式属性',
      title: '图谱水印url',
      description: '图谱水印url',
      setter: {
        concept: 'ImageSetter'
      }
    })
    backgroundImage: nasl.core.String;

    @Prop({
      group: '样式属性',
      title: '不重复显示水印',
      description: '只在右下角显示水印，不重复显示水印',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    backgroundImageNoRepeat: nasl.core.Boolean = false;

    @Prop({
      group: '交互属性',
      title: '显示工具栏',
      description: '是否显示工具栏',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    allowShowMiniToolBar: nasl.core.Boolean = true;

    @Prop({
      group: '交互属性',
      title: '显示自动布局按钮',
      description: '是否在工具栏中显示【自动布局】按钮',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    allowAutoLayoutIfSupport: nasl.core.Boolean = true;

    @Prop({
      group: '交互属性',
      title: '显示缩放菜单',
      description: '是否在右侧菜单栏显示放大缩小的按钮',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    allowShowZoomMenu: nasl.core.Boolean = true;
  
    @Prop({
      group: '交互属性',
      title: '显示刷新按钮',
      description: '是否在工具栏中显示【刷新】按钮',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    allowShowRefreshButton: nasl.core.Boolean = true;
  
    @Prop({
      group: '交互属性',
      title: '显示下载按钮',
      description: '是否显示下载按钮',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    allowShowDownloadButton: nasl.core.Boolean = true;

    @Prop({
      group: '交互属性',
      title: '下载文件名',
      description: '下载图片的文件名',
      setter: {
        concept: 'InputSetter'
      }
    })
    downloadImageFileName: nasl.core.String = '关系图';

    @Prop({
      group: '交互属性',
      title: '禁用缩放',
      description: '是否禁用缩放',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    disableZoom: nasl.core.Boolean = false;

    @Prop({
      group: '交互属性',
      title: '禁用节点拖拽',
      description: '是否禁用节点拖拽',
      setter: {
        concept: 'SwitchSetter'
      }
    })
    disableDragNode: nasl.core.Boolean = false;
  }
}