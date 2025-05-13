/// <reference types="@nasl/types" />
namespace extensions.rich_text_dir.viewComponents {
  const { Component, Prop, ViewComponent, Slot, Method, Event, ViewComponentOptions } = nasl.ui;

  @ExtensionComponent({
    type: 'pc',
    ideusage: {
      idetype: 'element',
    },
  })
  @Component({
    title: '富文本目录',
    description: '富文本目录',
  })
  export class RichTextDir extends ViewComponent {
    constructor(options?: Partial<RichTextDirOptions>) {
      super();
    }
  }

  export class RichTextDirOptions extends ViewComponentOptions {
    @Prop({
      title: '内容',
      description: '显示文本',
      setter: {
        concept: 'InputSetter',
      },
    })
    content: nasl.core.String = '';
    @Prop({
      title: '标题dom节点选择器',
      description: '根据富文本内容自动生成标题dom选择器',
      setter: {
        concept: 'InputSetter',
      },
    })
    titleSelector: nasl.core.String = 'h2';
    @Prop({
      title: '高度',
      setter: {
        concept: 'InputSetter',
      },
    })
    height: nasl.core.String = '400px';
    @Prop({
      title: '目录宽度',
      setter: {
        concept: 'InputSetter',
      },
    })
    categoryWidth: nasl.core.String = '240px';
  }
}
