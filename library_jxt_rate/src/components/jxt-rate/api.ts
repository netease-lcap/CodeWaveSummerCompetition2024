/// <reference types="@nasl/types" />
namespace extensions.library_jxt_rate.viewComponents {
  const { Component, Prop, ViewComponent, Slot, Method, Event, ViewComponentOptions } = nasl.ui;

  @ExtensionComponent({
    type: 'h5',
    ideusage: {
      idetype: 'element',
    }
  })
  @Component({
    title: '评分',
    description: '评分',
  })
  export class JxtRate extends ViewComponent {
    constructor(options?: Partial<JxtRateOptions>) {
      super();
    }
  }

  export class JxtRateOptions extends ViewComponentOptions {
     @Prop({
      title: '内容',
      description: '显示文本',
      setter: {
        concept: 'InputSetter'
      }
    })
    text: nasl.core.String = '';
  }
}