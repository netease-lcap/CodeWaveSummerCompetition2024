# cw_markdown

**组件名称：** md预览/编辑器

**组件类型：** h5/pc

**所属类别：** 组件

**标签：** ```<markdown></markdown>```

**主要特性**

`markdown` 组件是一个Markdown编辑器和渲染器，支持在网页和移动端同时使用。它允许用户在编辑器模式下编写Markdown，并在渲染器模式下查看Markdown的渲染结果。

## attrs

- **value：** 需要传入的值。默认为空字符串，用于初始化编辑器的内容或显示在渲染器中的Markdown文本。
- **mode：** markdown模式。默认为 `editor`。用户可以选择以下模式：
  - **editor：** 编辑器模式，允许用户输入和修改Markdown内容。
  - **viewer：** 渲染器模式，只显示Markdown内容的渲染结果，不允许编辑。

## events

- **change($event)：** 内容变化时触发。参数 `$event` 是变化后的值，表示编辑器中Markdown内容的最新状态。

**应用场景**

该组件适用于需要Markdown编辑功能的各种应用场景，如博客平台、文档编辑器等，提供了强大的Markdown编辑和显示功能，方便用户进行内容创建和展示。
