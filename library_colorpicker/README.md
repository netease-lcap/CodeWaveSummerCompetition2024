# 颜色选择器

**依赖库设计**

这个依赖库旨在提供前端UI颜色选择器，核心是封装 https://github.com/CNLHB/vue2-color-picker-gradient

**主要特性**

- **fdddf-colorpicker/支持多种选择方式及PC和H5的适配：**

## 组件一/逻辑一
颜色选择器 

**特性 1：** 支持多种选择方式渐变，hex，rgb，rgba等。

**特性 2：** change事件返回多种格式颜色值。


## fdddf-colorpicker

### 组件（组件名）

- **attrs**
    - value: 颜色值,hex 如"#000000"
    - title: 标题，默认为颜色选择器
    - isShowClose: 是否显示关闭按钮
    - isShowTitle: 是否显示标题

- **methods**
N/A

- **events**
    - change: 颜色值改变时触发，参数为颜色值对象，包括hex和rgba格式
    - close: 关闭颜色选择器时触发

### 逻辑（逻辑名）

N/A

## 应用演示链接

[查看示例演示](https://dev-testapp-qa.app.codewave.163.com/colorpicker_page)

![img](Screenshot%202024-03-17%20at%2000.09.10.png)
