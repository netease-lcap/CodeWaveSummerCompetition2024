### cw_large_file_uploader

#### large-file-split-uploader

**组件名称：** 大文件上传

**组件类型：** PC

**所属类别：** 组件

**标签：** ```<large-file-split-uploader />```

## 主要特性

`large-file-split-uploader` 组件用于在PC环境中上传大文件，支持文件的分片上传。该组件提供了丰富的配置选项，包括文件类型过滤、拖拽上传支持、文件访问策略等，以适应不同的使用场景和需求。

## attrs

### 数据属性

- **value (同步属性，模型支持)**：当前文件列表
- **name**：上传的文件字段名，默认为 `file`，支持自定义。
- **accept**：支持上传的文件类型，如 `.jpeg, .png, .gif`。未指定时默认支持所有类型。
- **with-credentials**：是否允许携带cookies。
- **data**：附加数据，以对象形式传入。
- **url-field**：请求返回的URL字段名。
- **headers**：请求头设置。

### 主要属性

- **converter**：设置请求转换器，支持 `JSON` 或 `URL字符串`。
- **display**：设置展示方式，支持 `inline`（行内展示）或 `block`（块级展示）。
- **description**：辅助文本，显示上传的数量、大小等信息。
- **showErrorMessage**：是否显示上传错误信息。
- **dragDescription**：拖拽区域的辅助文本。
- **access**：文件访问策略，支持 `public`（任何人可访问）和 `private`（登录用户可访问）。

### 交互属性

- **draggable**：是否启用拖拽上传。

### 状态属性

- **readonly**：设置为只读，禁止选择或输入。
- **disabled**：设置为禁用，禁止任何交互。

## events

- **before-upload**：上传前触发。
- **progress**：上传进度改变时触发。
- **success**：上传成功时触发。
- **error**：上传发生错误时触发。
- **remove**：点击删除按钮时触发。

## methods

- **select()**：选择文件进行上传。

## slots

- **default**：插槽，用于插入文本或HTML。

## 示例和使用场景

详细的示例代码和应用场景将在开发者文档中提供，以帮助开发者理解组件的使用方法和配置。
