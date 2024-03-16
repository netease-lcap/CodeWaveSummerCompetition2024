import org.junit.Test;

import java.util.regex.Pattern;

import static com.netease.lowcode.util.RegexUtils.*;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * @Date: 2024/3/7 - 03 - 07 - 23:18
 * @Description: PACKAGE_NAME
 * @version: 1.0
 */
public class RegexUtilsTest {

    @Test
    public void testCustomValidate() {
        // 测试符合正则表达式的情况
        String value = "123";
        String regex = "\\d+";
        boolean result = customValidate(value, regex);
        // 断言结果应为 true
        assertTrue(result);

        // 测试不符合正则表达式的情况
        value = "abc";
        result = customValidate(value, regex);
        // 断言结果应为 false
        assertFalse(result);
    }

    @Test
    public void testCheckMobile() {
        // 测试符合手机号码格式的情况
        String mobile = "+8613512345678";
        boolean result = checkMobile(mobile);
        // 断言结果应为 true
        assertTrue(result);

        // 测试不符合手机号码格式的情况
        mobile = "1234567890";
        result = checkMobile(mobile);
        // 断言结果应为 false
        assertFalse(result);
    }

    @Test
    public void testCheckIdCard() {
        // 测试符合身份证号码格式的情况
        String idCard = "340524198001011234";
        boolean result = checkIdCard(idCard);
        // 断言结果应为 true
        assertTrue(result);

        // 测试不符合身份证号码格式的情况
        idCard = "34052419800101123";
        result = checkIdCard(idCard);
        // 断言结果应为 false
        assertFalse(result);
    }

    @Test
    public void testIsEmail() {
        // 测试符合 Email 格式的情况
        String email = "wanggexin@example.com";
        boolean result = isEmail(email);
        // 断言结果应为 true
        assertTrue(result);

        // 测试不符合 Email 格式的情况
        email = "wanggexin@";
        result = isEmail(email);
        // 断言结果应为 false
        assertFalse(result);
    }

    @Test
    public void testCheckPostcode() {
        // 有效的邮政编码
        assertTrue(checkPostcode("454000"));
        // 无效的邮政编码
        assertFalse(checkPostcode("1234"));
    }

    @Test
    public void testCheckDate() {
        // 有效的日期
        assertTrue(checkDate("1992-09-03"));
        assertTrue(checkDate("1992.09.03"));
        // 无效的日期
        assertFalse(checkDate("199209-31"));
        assertFalse(checkDate("199209.31"));
    }

    @Test
    public void testCheckDateTime() {
        // 有效的日期时间
        assertTrue(checkDateTime("1992-09-03 12:22:21"));
        // 无效的日期时间
        assertFalse(checkDateTime("1992-09-03 25:22:21"));
        assertFalse(checkDateTime("1992.09.03 12:22:21"));
    }

    @Test
    public void testCheckDigit() {
        // 有效的数字
        assertTrue(checkDigit("123"));
        assertTrue(checkDigit("-456"));
        // 无效的数字
        assertFalse(checkDigit("123.45"));
        assertFalse(checkDigit("abc"));
    }

    @Test
    public void testValidateInterAndZero() {
        // 有效的整数（包括 0）
        assertTrue(validateInterAndZero("0"));
        assertTrue(validateInterAndZero("123"));
        // 无效的整数
        assertFalse(validateInterAndZero("123.45"));
        assertFalse(validateInterAndZero("abc"));
    }

    @Test
    public void testIsInteger() {
        // 有效的整数
        assertTrue(isInteger("123"));
        assertTrue(isInteger("-456"));
        assertTrue(isInteger("0"));
        // 无效的整数
        assertFalse(isInteger("123.45"));
        assertFalse(isInteger("abc"));
    }

    @Test
    public void testCheckDecimals() {
        // 有效的小数
        String decimal = "1.23";
        boolean result = checkDecimals(decimal);
        // 预期结果为 true
        assertTrue(result);

        // 无效的小数
        String invalidDecimal = "1.23.45";
        result = checkDecimals(invalidDecimal);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsPositiveDouble() {
        // 有效的正数小数
        String positiveDouble = "+233.30";
        boolean result = isPositiveDouble(positiveDouble);
        // 预期结果为 true
        assertTrue(result);

        // 无效的正数小数
        String invalidPositiveDouble = "233.30-";
        result = isPositiveDouble(invalidPositiveDouble);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void  testCheckChinese() {
        // 有效的中文字符
        String chinese = "王";
        boolean result = checkChinese(chinese);
        // 预期结果为 true
        assertTrue(result);

        // 无效的中文字符
        String invalidChinese = "Wang";
        result = checkChinese(invalidChinese);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testCheckChar() {
        // 有效的字母
        String abc = "abc";
        boolean result = checkChar(abc);
        // 预期结果为 true
        assertTrue(result);

        // 无效的字母
        String invalidChar = "abc@";
        result = checkChar(invalidChar);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testCheckBlankSpace() {
        // 有效的空白字符
        String blankSpace = "   ";
        boolean result = checkBlankSpace(blankSpace);
        // 预期结果为 true
        assertTrue(result);

        // 无效的空白字符
        String invalidBlankSpace = "abc";
        result = checkBlankSpace(invalidBlankSpace);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidMacAddress() {
        // 有效的 MAC 地址
        String macAddress = "01:23:45:67:89:ab";
        boolean result = isValidMacAddress(macAddress);
        // 预期结果为 true
        assertTrue(result);

        // 无效的 MAC 地址
        String invalidMacAddress = "0123-45-67-89-ab";
        result = isValidMacAddress(invalidMacAddress);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidIPv4() {
        // 有效的 IPv4 地址
        String ipv4 = "192.168.0.1";
        boolean result = isValidIPv4(ipv4);
        // 预期结果为 true
        assertTrue(result);

        // 无效的 IPv4 地址
        String invalidIPv4 = "192.168.0.256";
        result = isValidIPv4(invalidIPv4);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidIPv6() {
        // 有效的 IPv6 地址
        String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        boolean result = isValidIPv6(ipv6);
        // 预期结果为 true
        assertTrue(result);

        // 无效的 IPv6 地址
        String invalidIPv6 = "0db8:8a3:0000:0000:8a2e:0370:73";
        result = isValidIPv6(invalidIPv6);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testFind() {
        // 有效的字符串和正则表达式
        String str = "abc";
        String regex = "abc";
        boolean result = find(str, regex);
        // 预期结果为 true
        assertTrue(result);

        // 有效的字符串和无效的正则表达式
        str = "abc";
        regex = "def";
        result = find(str, regex);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidPasswordLength8Easy() {
        // 有效的密码（长度为 8，包含字母和数字）
        String password = "Password123";
        boolean result = isValidPasswordLength8Easy(password);
        // 预期结果为 true
        assertTrue(result);

        // 无效的密码（长度小于 8）
        password = "pass";
        result = isValidPasswordLength8Easy(password);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidPasswordLength6Easy() {
        // 有效的密码（长度为 6，包含字母和数字）
        String password = "Pass123";
        boolean result = isValidPasswordLength6Easy(password);
        // 预期结果为 true
        assertTrue(result);

        // 无效的密码（长度小于 6）
        password = "pas";
        result = isValidPasswordLength6Easy(password);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidPasswordLength8Difficult() {
        // 有效的密码（长度为 8，包含字母、数字和特殊字符）
        String password = "Password@123!";
        boolean result = isValidPasswordLength8Difficult(password);
        // 预期结果为 true
        assertTrue(result);

        // 无效的密码（长度小于 8）
        password = "pass@";
        result = isValidPasswordLength8Difficult(password);
        // 预期结果为 false
        assertFalse(result);
    }

    @Test
    public void testIsValidPasswordLength6Difficult() {
        // 有效的密码（长度为 6，包含字母、数字和特殊字符）
        String password = "Pass@12";
        boolean result = isValidPasswordLength6Difficult(password);
        // 预期结果为 true
        assertTrue(result);

        // 无效的密码（长度小于 6）
        password = "pas@";
        result = isValidPasswordLength6Difficult(password);
        // 预期结果为 false
        assertFalse(result);
    }
}
