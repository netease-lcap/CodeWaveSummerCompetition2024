package newease.lowcode.data.masking;


import com.netease.lowcode.core.annotation.NaslLogic;
import newease.lowcode.utils.CustomDataMaskingUtil;
import org.springframework.stereotype.Component;

/**
 *所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串不会返回原字符主要考虑数据安全问题避免泄露
 *当前类主要是用户单条数据进行脱敏
 * @author 19153
 */
@Component
public class SingleDataMasking {

    /**
     * 手机号脱敏 例:183****5376
     * @param phone 手机号
     * @return
     */
    @NaslLogic
    public static String mobilePhoneSingleDataMasking(String phone){
        return CustomDataMaskingUtil.mobilePhone(phone);
    }

    /**
     * 中国姓名脱敏 例:王**
     * @param chineseName  中国姓名
     * @return
     */
    @NaslLogic
    public String chineseNameSingleDataMasking(String chineseName){
        return CustomDataMaskingUtil.chineseName(chineseName);
    }

    /**
     * 身份证号码脱敏 例:410***********0093
     * @param idCardNum 身份证号码
     * @param startSaveLength 需要保留的身份证号码开头的长度
     * @param endSaveLength 需要保留的身份证号码末尾的长度
     * @return
     */
    @NaslLogic
    public String idCardNumSingleDataMasking(String idCardNum, Integer startSaveLength, Integer endSaveLength){
        return CustomDataMaskingUtil.idCardNum(idCardNum,startSaveLength,endSaveLength);
    }

    /**
     * 邮箱脱敏 例:w***********@163.com
     * @param email 邮箱
     * @return
     */
    @NaslLogic
    public String emailSingleDataMasking(String email){

        return CustomDataMaskingUtil.email(email);
    }

    /**
     * 银行卡脱敏 例:6200 **** **** 1785
     * @param bankCard 银行卡
     * @return
     */
    @NaslLogic
    public String bankCardSingleDataMasking(String bankCard){
        return CustomDataMaskingUtil.bankCard(bankCard);
    }

    /**
     * 地址脱敏 例:山东省泰安市************
     * @param address 地址
     * @param sensitiveSize 需要格式化为*的长度，从后往前
     * @return
     */
    @NaslLogic
    public String addressSingleDataMasking(String address,Integer sensitiveSize){
        return CustomDataMaskingUtil.address(address,sensitiveSize);
    }

    /**
     * 密码脱敏 例:**************
     * @param password 密码
     * @return
     */
    @NaslLogic
    public String passwordSingleDataMasking(String password){
        return CustomDataMaskingUtil.password(password);
    }

    /**
     * ipv4脱敏 例:192.*.*.*
     * @param ipv4 ipv4
     * @return
     */
    @NaslLogic
    public String ipv4SingleDataMasking(String ipv4){
        return CustomDataMaskingUtil.ipv4(ipv4);
    }

    /**
     * ipv6脱敏 例:FC00:*:*:*:*:*:*:*
     * @param ipv6 ipv6
     * @return
     */
    @NaslLogic
    public String ipv6SingleDataMasking(String ipv6){
        return CustomDataMaskingUtil.ipv6(ipv6);
    }


    /**
     * 自定义脱敏 可自定义脱敏内容、开始位置、脱敏长度、脱敏字符
     * @param text 脱敏文本
     * @param start  开始位置
     * @param desensitizationLength  长度
     * @param maskChar 脱敏字符
     * @return
     */
    @NaslLogic
    public String generalSingleDataMasking(String text, Integer start, Integer desensitizationLength, String maskChar){
        return CustomDataMaskingUtil.customDesensitization(text, start-1, desensitizationLength, maskChar);
    }
}
