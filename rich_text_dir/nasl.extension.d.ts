declare namespace extensions.rich_text_dir.viewComponents {
  const ViewComponent: typeof nasl.ui.ViewComponent, ViewComponentOptions: typeof nasl.ui.ViewComponentOptions;
  export class RichTextDir extends ViewComponent {
    constructor(options?: Partial<RichTextDirOptions>);
  }
  export class RichTextDirOptions extends ViewComponentOptions {
    /**
     * 内容
     * 显示文本
     */
    content: nasl.core.String;
    /**
     * 标题dom节点选择器
     * 根据富文本内容自动生成标题dom选择器
     */
    titleSelector: nasl.core.String;
    /**
     * 高度
     */
    height: nasl.core.String;
    /**
     * 目录宽度
     */
    categoryWidth: nasl.core.String;
  }
  export {};
}