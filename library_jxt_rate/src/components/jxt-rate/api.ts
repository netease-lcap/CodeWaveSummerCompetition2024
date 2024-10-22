/// <reference types="@nasl/types" />
namespace extensions.library_jxt_rate.viewComponents {
  const { Component, Prop, ViewComponent, Slot, Method, Event, ViewComponentOptions } = nasl.ui;

  @ExtensionComponent({
    type: 'h5',
    ideusage: {
      idetype: 'element',
    },
  })
  @Component({
    title: '评分',
    description: '评分组件',
  })
  export class JxtRate extends ViewComponent {
    constructor(options?: Partial<JxtRateOptions>) {
      super();
    }
  }

  export class JxtRateOptions extends ViewComponentOptions {
    @Prop({
      group: '数据属性',
      title: '评分值',
      sync: true,
      docDescription: '当前选择的值',
      setter: {
        concept: 'NumberInputSetter',
        max: 20,
      },
    })
    value: nasl.core.Decimal = 0;

    @Prop({
      group: '数据属性',
      title: '最大分数',
      docDescription: '评分组件的最大分数',
      setter: {
        concept: 'NumberInputSetter',
        max: 20,
      },
    })
    max: nasl.core.Decimal = 5;

    @Prop({
      group: '主要属性',
      title: '展示辅助文本',
      description: '在评分后展示辅助文字',
      docDescription: '在评分后展示辅助文字',
      setter: {
        concept: 'SwitchSetter',
      },
    })
    showText: nasl.core.Boolean = false;

    @Prop({
      group: '主要属性',
      title: '辅助文本',
      description: '辅助说明的文本信息，在展示辅助文本开关打开的情况下才会生效',
      docDescription: '辅助文字设置。在展示辅助文字开关打开的情况下才会生效',
    })
    texts: Array<nasl.core.String> = ['极差', '失望', '一般', '满意', '惊喜'];

    @Prop({
      group: '交互属性',
      title: '可半选',
      description: '是否允许半选',
      docDescription: '是否允许半选',
      setter: {
        concept: 'SwitchSetter',
      },
    })
    allowHalf: nasl.core.Boolean = false;

    @Prop({
      group: '交互属性',
      title: '可清除',
      description: '可选中后再次点击以清除选中',
      docDescription: '是否允许选中后点击清除',
      setter: {
        concept: 'SwitchSetter',
      },
    })
    clearable: nasl.core.Boolean = false;

    @Prop({
      group: '状态属性',
      title: '只读',
      description: '正常显示，但禁止选择/输入',
      docDescription: '正常显示，但禁止选择或输入',
      setter: {
        concept: 'SwitchSetter',
      },
    })
    readonly: nasl.core.Boolean = false;

    @Prop({
      group: '状态属性',
      title: '预览',
      description: '显示预览态',
      docDescription: '',
      setter: {
        concept: 'SwitchSetter',
      },
    })
    preview: nasl.core.Boolean = false;

    @Event({
      title: '改变后',
      description: '修改时触发',
    })
    onChange: (event: { value: nasl.core.Decimal; oldValue: nasl.core.Decimal }) => any;
  }
}
