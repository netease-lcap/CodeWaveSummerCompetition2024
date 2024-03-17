import com.netease.lowcode.validation.RegexValidatorResult;
import junit.framework.TestCase;
import org.junit.Test;

import static com.netease.lowcode.util.RegexUtils.checkMobile;
import static com.netease.lowcode.util.RegexUtils.customValidate;
import static org.junit.Assert.*;

/**
 * @Date: 2024/3/17 - 03 - 17 - 16:54
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class RegexValidatorUtilTest {
    @Test
    public void testCustomValidate() {
        // 定义测试用例
        String value = "12345678";
        String regex = "\\d+";

        // 调用待测试的方法
        RegexValidatorResult result = customValidate(value, regex);

        // 断言结果为 true
        assertTrue(result.isIsValid());
        assertEquals("输入值符合正则表达式", result.getErrorMessage());

        // 测试空值情况
        result = customValidate(null, regex);
        assertFalse(result.isIsValid());
        assertEquals("输入值不能为空", result.getErrorMessage());

        // 测试空正则表达式情况
        result = customValidate(value, null);
        assertFalse(result.isIsValid());
        assertEquals("正则表达式不能为空", result.getErrorMessage());

        // 测试无效正则表达式情况
        regex = "(?<group>(abc)";
        result = customValidate(value, regex);
        assertFalse(result.isIsValid());
        System.out.println("result.getErrorMessage = " + result.getErrorMessage());
    }

    @Test
    public void testCheckMobile() {
        // 定义测试用例
        String value = "+8618303905376";

        // 调用待测试的方法
        RegexValidatorResult result = checkMobile(value);

        // 断言结果为 true
        assertTrue(result.isIsValid());
        assertEquals("输入值符合正则表达式", result.getErrorMessage());

        // 断言结果为 Fales
        value = "1873039057444";
        result = checkMobile(value);
        assertFalse(result.isIsValid());
        assertEquals("输入值不符合正则表达式", result.getErrorMessage());

        // 测试空值情况
        result = checkMobile(null);
        assertFalse(result.isIsValid());
        assertEquals("输入值不能为空", result.getErrorMessage());

    }

}
