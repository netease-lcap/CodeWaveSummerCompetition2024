# 依赖库名称
作者： superName-w
该依赖库提供了一组工具方法，用于处理与农历相关的各种转换和计算，包括阳历与农历的互转、获取农历月份信息、节气、闰月信息、生肖以及判断传统节日等。

``convertSolarToLunar(String solarDateString)`

- 作用：将阳历日期转换为农历日期。
  - 入参：阳历日期字符串（solarDateString），格式为 `yyyy-MM-dd`。
  - 返回值：LunarResult对象，包含转换结果的布尔值、农历日期字符串和操作结果描述。

`convertLunarToSolar(Integer lunarYear, Integer lunarMonth, Integer lunarDay)`

- 作用：将农历日期转换为阳历日期。
  - 入参：农历年份（lunarYear）、农历月份（lunarMonth）、农历日期（lunarDay）。
  - 返回值：LunarResult对象，包含转换结果的布尔值、阳历日期字符串和操作结果描述。

`getChineseMonthInfo(String solarDateString)`

- 作用：获取阳历日期对应的农历月份信息，包括月份名称、是否为闰月以及月份大小。
  - 入参：阳历日期字符串（solarDateString），格式为 `yyyy-MM-dd`。
  - 返回值：LunarResult对象，包含获取结果的布尔值、农历月份信息字符串和操作结果描述。

`getTermInfo(String solarDateString)`

- 作用：根据阳历日期获取对应的节气。
  - 入参：阳历日期字符串（solarDateString），格式为 `yyyy-MM-dd`。
  - 返回值：LunarResult对象，包含获取结果的布尔值、节气名称字符串和操作结果描述。

`getLeapMonthInfo(Integer lunarYear)`

- 作用：获取农历年份的闰月信息，包括闰月的月份和大小。
  - 入参：农历年份（lunarYear）。
  - 返回值：LunarResult对象，包含获取结果的布尔值、闰月信息字符串和操作结果描述。

`getChineseZodiac(Integer lunarYear)`

- 作用：获取农历年份对应的生肖信息。
  - 入参：农历年份（lunarYear）。
  - 返回值：LunarResult对象，包含获取结果的布尔值、生肖信息字符串和操作结果描述。

`getFestivals(Integer lunarYear, Integer lunarMonth, Integer lunarDay)`

- 作用：判断农历日期是否为传统节日，并返回节日名称。
  - 入参：农历年份（lunarYear）、农历月份（lunarMonth）、农历日期（lunarDay）。
  - 返回值：LunarResult对象，包含判断结果的布尔值、节日名称字符串和操作结果描述。

`getLunarDaysDifference(Integer lunarYear1, Integer lunarMonth1, Integer lunarDay1, Integer lunarYear2, Integer lunarMonth2, Integer lunarDay2)`

- 作用：计算两个农历日期之间的天数差。
  - 入参：两个农历日期的年份（lunarYear1, lunarYear2）、月份（lunarMonth1, lunarMonth2）和日期（lunarDay1, lunarDay2）。
  - 返回值：LunarResult对象，包含计算结果的布尔值、天数差字符串和操作结果描述。

## 使用步骤说明

1. 应用引用依赖库

2. 逻辑调用示例截图

   ![1709736256327](%E8%B0%83%E7%94%A8%E7%A4%BA%E4%BE%8B.jpg)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
