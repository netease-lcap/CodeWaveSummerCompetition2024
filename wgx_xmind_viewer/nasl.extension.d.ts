declare namespace extensions.wgx_xmind_viewer.viewComponents {
  const ViewComponent: typeof nasl.ui.ViewComponent, ViewComponentOptions: typeof nasl.ui.ViewComponentOptions;
  export class XmindViewer extends ViewComponent {
    constructor(options?: Partial<XmindViewerOptions>);
  }
  export class XmindViewerOptions extends ViewComponentOptions {
    /**
     * xmind文件地址
     * xmind文件地址
     */
    fileUrl: nasl.core.String;
  }
  export {};
}