# lxy_antv_table_view_library

**依赖库设计**

这个依赖库使用antv/s2,可以实现普通和树形表格，支持虚拟滚动合并表头。

**主要特性**

- **组件一：** 

## 组件一
实现合并表头的表格

**特性 1：**  合并单元格
**特性 2：**  支持树形和普通2种形式



...

## 使用说明

### 组件（组件名）

- **attrs**
  - dataSource: 数据源
  - fields: 表头字段， 表头相关设置rows代表行头，columns代表列头，values 代表取的值。 如{rows:['province', 'city'],'columns':['type', 'sub_type'], 'values':['number']}
  - meta: 字段映射 字段与label的映射关系,如 [{field:'number',name:'数量'}]
  - hierarchyType: 表格类型
  - width: 表格宽度
  - height: 表格高度
  - cellWidth: 表格单元格宽度度
  - cellHeight: 表格单元格高度




## 应用演示链接

[查看示例演示](示例演示链接)
