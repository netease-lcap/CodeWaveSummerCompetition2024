# 依赖库名称
作者： superName-w
该依赖库定义了多个方法用于数学计算的逻辑，如绝对值、取整、最小值、最大值、加法、减法、乘法、除法等。

注意：取舍方式统一为Integer类型（0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整），如果为其他结果则会抛出异常。如果设置为0精度则会失效，则不会进行取舍。

- `absInteger(Long a)`：
- 作用：返回一个整数的绝对值。
  - 入参：要计算绝对值的整数（a）。
  - 入参类型：Long。
  - 返回值：整数的绝对值，类型为 Long。
  - 特殊说明：如果输入参数为 null，则抛出 NullPointerException 异常。
- `absDecimal(Double a)`：
  - 作用：返回一个小数的绝对值。
  - 入参：要计算绝对值的小数（a）。
  - 入参类型：Double。
  - 返回值：小数的绝对值，类型为 Double。
  - 特殊说明：如果输入参数为 null，则抛出 NullPointerException 异常。
- `floor(Double a)`：
  - 作用：返回小于等于给定参数的最大整数。
  - 入参：要进行向下取整的小数（a）。
  - 入参类型：Double。
  - 返回值：向下取整后的结果，类型为 Double。
  - 特殊说明：如果输入参数为 NaN、正无穷大或负无穷小，则抛出 IllegalArgumentException 异常。
- `ceil(Double a)`：
  - 作用：返回大于等于给定参数的最小整数。
  - 入参：要进行向上取整的小数（a）。
  - 入参类型：Double。
  - 返回值：向上取整后的结果，类型为 Double。
  - 特殊说明：如果输入参数为 NaN、正无穷大或负无穷小，则抛出 IllegalArgumentException 异常
- `minInteger(Long a, Long b)`：
  - 作用：返回两个整数中较小的数。
  - 入参：两个整数 a 和 b。
  - 入参类型：Long 类型的两个参数。
  - 返回值：两个整数中较小的数值，类型为 Long。
  - 特殊说明：如果任一参数为 null，则抛出 NullPointerException。
- `maxInteger(Long a, Long b)`：
  - 作用：返回两个整数中较大的数。
  - 入参：两个整数 a 和 b。
  - 入参类型：Long 类型的两个参数。
  - 返回值：两个整数中较大的数值，类型为 Long。
  - 特殊说明：如果任一参数为 null，则抛出 NullPointerException。
- `minDecimal(Double a, Double b)`：
  - 作用：返回两个小数中较小的数。
  - 入参：两个小数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两个小数中较小的数值，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 NullPointerException。
- `maxDecimal(Double a, Double b)`：
  - 作用：返回两个小数中较大的数。
  - 入参：两个小数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两个小数中较大的数值，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 NullPointerException。
- `toFixed(Double a, Integer digits)`：
  - 作用：将小数转化为小数点后指定位数的字符串并进行四舍五入 例如传入：a=123.456，digits=2，则返回：123.46
  - 入参：待格式化的小数 a 和小数点后的位数 digits。
  - 入参类型：Double 类型的小数和 Integer 类型的位数。
  - 返回值：格式化后的字符串。
  - 特殊说明：如果 a 或 digits 为 null，或者 digits 不在 0 到 15 之间，则抛出相应的异常。
- `add(Double a, Double b)`：
  - 作用：计算两数之和。
  - 入参：两个加数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之和，类型为 Double。
  - 特殊说明：如果任一加数为 null，则抛出 IllegalArgumentException。
- `addRounded(Double a, Double b)`：
  - 作用：计算两数之和，保留两位小数，四舍五入。
  - 入参：两个加数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之和，保留两位小数，四舍五入后的值，类型为 Double。
  - 特殊说明：如果任一加数为 null，则抛出 IllegalArgumentException。
- `addCeiling(Double a, Double b)`：
  - 作用：计算两数之和，并向上取整，返回取整后的小数。
  - 入参：两个加数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之和，向上取整后的值，类型为 Double。
  - 特殊说明：如果任一加数为 null，则抛出 IllegalArgumentException。
- `addFloor(Double a, Double b)`：
  - 作用：计算两数之和，并向下取整，返回取整后的小数。
  - 入参：两个加数 a 和 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之和，向下取整后的值，类型为 Double。
  - 特殊说明：如果任一加数为 null，则抛出 IllegalArgumentException。
- `addCustomScale(Double a, Double b, Integer scale, Integer roundingType)`：
  - 作用：计算两数之和，自定义保留小数位数和取舍方式。
  - 入参：两个加数 a 和 b，结果的精度 scale，取舍方式 roundingType。取舍方式统一为Integer类型（0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整），如果为其他结果则会抛出异常。如果设置为0精度则会失效则不进行取舍。
  - 入参类型：Double 类型的两个参数，Integer 类型的精度和取舍方式。
  - 返回值：根据给定的精度和取舍方式计算的两数之和，类型为 Double。
  - 特殊说明：如果任一加数、精度或取舍方式为 null，或者精度不在有效范围内，则抛出相应的异常。
- `subtract(Double a, Double b)`：
  - 作用：计算两数之差。
  - 入参：被减数 a 和减数 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之差，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 IllegalArgumentException。
- `subtractRounded(Double a, Double b)`：
  - 作用：计算两数之差，保留两位小数，四舍五入。
  - 入参：被减数 a 和减数 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之差，保留两位小数，四舍五入后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 IllegalArgumentException。
- `subtractCeiling(Double a, Double b)`：
  - 作用：计算两数之差，并向上取整，返回取整后的小数。
  - 入参：被减数 a 和减数 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之差，向上取整后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 IllegalArgumentException。
- `subtractFloor(Double a, Double b)`：
  - 作用：计算两数之差，并向下取整，返回取整后的小数。
  - 入参：被减数 a 和减数 b。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之差，向下取整后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，则抛出 IllegalArgumentException。
- `subtractCustomScale(Double a, Double b, Integer scale, Integer roundingType)`：
  - 作用：计算两数之差，自定义保留小数位数和取舍方式。
  - 入参：被减数 a 和减数 b，结果的精度 scale，取舍方式 roundingType。取舍方式统一为Integer类型（0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整），如果为其他结果则会抛出异常。如果设置为0精度则会失效则不进行取舍。
  - 入参类型：Double 类型的两个参数，Integer 类型的精度和取舍方式。
  - 返回值：根据给定的精度和取舍方式计算的两数之差，类型为 Double。
  - 特殊说明：如果任一参数、精度或取舍方式为 null，或者精度不在有效范围内，则抛出相应的异常。
- `divide(Double dividend, Double divisor)`：
  - 作用：计算两数之商。
  - 入参：被除数 dividend 和除数 divisor。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之商，类型为 Double。
  - 特殊说明：如果任一参数为 null，或者 divisor 为 0，则抛出相应的异常。
- `divideRounded(Double dividend, Double divisor)`：
  - 作用：计算两数之商，保留两位小数，四舍五入。
  - 入参：被除数 dividend 和除数 divisor。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之商，保留两位小数，四舍五入后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，或者 divisor 为 0，则抛出相应的异常。
- `divideCeiling(Double dividend, Double divisor)`：
  - 作用：计算两数之商，并向上取整，返回取整后的小数。
  - 入参：被除数 dividend 和除数 divisor。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之商，向上取整后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，或者 divisor 为 0，则抛出相应的异常。
- `divideFloor(Double dividend, Double divisor)`：
  - 作用：计算两数之商，并向下取整，返回取整后的小数。
  - 入参：被除数 dividend 和除数 divisor。
  - 入参类型：Double 类型的两个参数。
  - 返回值：两数之商，向下取整后的值，类型为 Double。
  - 特殊说明：如果任一参数为 null，或者 divisor 为 0，则抛出相应的异常。
- `divideCustomScale(Double dividend, Double divisor, Integer scale, Integer roundingType)`：
  - 作用：计算两数之商，支持自定义保留小数位数和多种取舍方式。
  - 入参：被除数 `dividend`，除数 `divisor`，结果的精度 `scale`（小数点后的位数），取舍方式 `roundingType`，取舍方式统一为Integer类型（0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整），如果为其他结果则会抛出异常。如果设置为0精度则会失效则不进行取舍。
  - 入参类型：方法接受两个 `Double` 类型的数值作为被除数和除数，一个 `Integer` 类型的数值作为精度，以及一个表示取舍方式的 `Integer`。
  - 返回值：返回根据给定精度和取舍方式计算出的两数之商，结果为 `Double` 类型。
  - 特殊说明：方法首先检查所有参数是否为 `null`，以及精度是否为非负整数和取舍方式是否有效。如果参数无效，则抛出 `IllegalArgumentException`。然后，根据取舍方式创建对应的 `RoundingMode` 对象，并使用 `BigDecimal` 类的 `divide` 方法进行精确除法运算。
- `multiply(Double a, Double b)`：
  - 作用：计算两数之积。
  - 入参：第一个乘数 `a`，第二个乘数 `b`。
  - 入参类型：方法接受两个 `Double` 类型的数值作为乘数。
  - 返回值：返回两数之积，结果为 `Double` 类型。
  - 特殊说明：方法检查乘数是否为 `null`。如果任一乘数为 `null`，则抛出 `IllegalArgumentException`。然后，使用 `BigDecimal` 类的 `multiply` 方法进行精确乘法运算。
- `multiplyRounded(Double a, Double b)`：
  - 作用：计算两数之积，结果保留两位小数，进行四舍五入。
  - 入参：第一个乘数 `a`，第二个乘数 `b`。
  - 入参类型：方法接受两个 `Double` 类型的数值作为乘数。
  - 返回值：返回保留两位小数，四舍五入后的两数之积，结果为 `Double` 类型。
  - 特殊说明：方法检查乘数是否为 `null`。如果任一乘数为 `null`，则抛出 `IllegalArgumentException`。然后，使用 `BigDecimal` 类的 `setScale` 方法设置结果的精度和取舍方式为四舍五入。
- `multiplyCeiling(Double a, Double b)`：
  - 作用：计算两数之积，并向上取整，返回取整后的小数。
  - 入参：第一个乘数 `a`，第二个乘数 `b`。
  - 入参类型：方法接受两个 `Double` 类型的数值作为乘数。
  - 返回值：返回向上取整后的两数之积，结果为 `Double` 类型。
  - 特殊说明：方法检查乘数是否为 `null`。如果任一乘数为 `null`，则抛出 `IllegalArgumentException`。取舍方式为向上取整。
- `multiplyFloor(Double a, Double b)`：
  - 作用：计算两数之积，并向下取整，返回取整后的小数。
  - 入参：第一个乘数 `a`，第二个乘数 `b`。
  - 入参类型：方法接受两个 `Double` 类型的数值作为乘数。
  - 返回值：返回向下取整后的两数之积，结果为 `Double` 类型。
  - 特殊说明：方法检查乘数是否为 `null`。如果任一乘数为 `null`，则抛出 `IllegalArgumentException`。然后，取舍方式为向下取整。
- `multiplyCustomScale(Double a, Double b, Integer scale, Integer roundingType)`：
  - 作用：计算两数之积，支持自定义保留小数位数和多种取舍方式。
  - 入参：第一个乘数 `a`，第二个乘数 `b`，结果的精度 `scale`（小数点后的位数），取舍方式 `roundingType`，取舍方式统一为Integer类型（0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整），如果为其他结果则会抛出异常。如果设置为0精度则会失效则不进行取舍。
  - 入参类型：方法接受两个 `Double` 类型的数值作为乘数，一个 `Integer` 类型的数值作为精度，以及一个表示取舍方式的 `Integer`。
  - 返回值：返回根据给定精度和取舍方式计算出的两数之积，结果为 `Double` 类型。
  - 特殊说明：方法首先检查所有参数是否为 `null`，以及精度是否为非负整数和取舍方式是否有效。如果参数无效，则抛出 `IllegalArgumentException`。然后，根据取舍方式创建对应的 `RoundingMode` 对象，并使用 `BigDecimal` 类的 `setScale` 方法进行精确乘法运算，并设置结果的精度和取舍方式。

## 使用步骤说明

1. 应用引用依赖库

2. 逻辑调用示例截图

   ![1709736256327](使用示例.png)

## 应用演示链接

使用了本依赖库的应用的链接。

无法发布生产环境目前只能提供测试环境，考虑测试环境随时失效此处并未提供
