package newease.lowcode.data.masking;

import com.netease.lowcode.core.annotation.NaslLogic;
import newease.lowcode.utils.CustomDataMaskingUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 所有数据脱敏逻辑如果入参为非法参数或者为空都会返回空白字符串不会返回原字符主要考虑数据安全问题避免泄露
 * 当前类主要是用于多条数据进行脱敏
 * @author 19153
 */
@Component
public class BulkDataMasking {

    /**
     * 手机号批量脱敏 例:183****5376
     * @param phoneList 手机号列表
     * @return
     */
    @NaslLogic
    public List<String> mobilePhoneBulkDataMasking (List<String> phoneList) {
        return phoneList.stream()
                // 使用 map 操作将每个手机号进行脱敏
                .map(phone -> CustomDataMaskingUtil.mobilePhone(phone))
                // 将脱敏后的手机号收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 中国姓名批量脱敏 例:张*
     * @param chineseNameList 中国姓名列表
     * @return
     */
    @NaslLogic
    public List<String> chineseNameBulkDataMasking(List<String> chineseNameList) {
        return chineseNameList.stream()
                // 使用 map 操作将每个中文姓名进行脱敏
                .map(chineseName -> CustomDataMaskingUtil.chineseName(chineseName))
                // 将脱敏后的中文姓名收集到新的列表中
                .collect(Collectors.toList());
    }
    /**
     * 身份证号码批量脱敏 例:410***********0093
     * @param idCardNumList 身份证号码
     * @param startSaveLength 需要保留的身份证号码开头的长度
     * @param endSaveLength 需要保留的身份证号码末尾的长度
     * @return
     */
    @NaslLogic
    public List<String> idCardNumBulkDataMasking(List<String> idCardNumList, Integer startSaveLength, Integer endSaveLength) {
        return idCardNumList.stream()
                // 使用 map 操作将每个身份证号码进行脱敏
                .map(idCardNum -> CustomDataMaskingUtil.idCardNum(idCardNum, startSaveLength, endSaveLength))
                // 将脱敏后的身份证号码收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 邮箱批量脱敏 例:w***********@163.com
     * @param emailList 邮箱
     * @return
     */
    @NaslLogic
    public List<String> emailBulkDataMasking(List<String> emailList) {
        return emailList.stream()
                // 使用 map 操作将每个邮箱进行脱敏
                .map(email -> CustomDataMaskingUtil.email(email))
                // 将脱敏后的邮箱收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 银行卡批量脱敏 例:6200 **** **** 1785
     * @param bankCardList 银行卡
     * @return
     */
    @NaslLogic
    public List<String> bankCardBulkDataMasking(List<String> bankCardList) {
        return bankCardList.stream()
                // 使用 map 操作将每个银行卡进行脱敏
                .map(bankCard -> CustomDataMaskingUtil.bankCard(bankCard))
                // 将脱敏后的银行卡收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 地址批量脱敏 例:山东省泰安市************
     * @param addressList 地址
     * @param sensitiveSize 需要格式化为*的长度，从后往前
     * @return
     */
    @NaslLogic
    public List<String> addressBulkDataMasking(List<String> addressList, Integer sensitiveSize) {
        return addressList.stream()
                // 使用 map 操作将每个地址进行脱敏
                .map(address -> CustomDataMaskingUtil.address(address, sensitiveSize))
                // 将脱敏后的地址收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 密码批量脱敏 例:**************
     * @param passwordList 密码
     * @return
     */
    @NaslLogic
    public List<String> passwordBulkDataMasking(List<String> passwordList) {
        return passwordList.stream()
                // 使用 map 操作将每个密码进行脱敏
                .map(password -> CustomDataMaskingUtil.password(password))
                // 将脱敏后的密码收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * ipv4批量脱敏 例:192.*.*.*
     * @param ipv4List ipv4
     * @return
     */
    @NaslLogic
    public List<String> ipv4BulkDataMasking(List<String> ipv4List) {
        return ipv4List.stream()
                // 使用 map 操作将每个 ipv4 进行脱敏
                .map(ipv4 -> CustomDataMaskingUtil.ipv4(ipv4))
                // 将脱敏后的 ipv4 收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * ipv6批量脱敏 例:FC00:*:*:*:*:*:*:*
     * @param ipv6List ipv6
     * @return
     */
    @NaslLogic
    public List<String> ipv6BulkDataMasking(List<String> ipv6List) {
        return ipv6List.stream()
                // 使用 map 操作将每个 ipv6 进行脱敏
                .map(ipv6 -> CustomDataMaskingUtil.ipv6(ipv6))
                // 将脱敏后的 ipv6 收集到新的列表中
                .collect(Collectors.toList());
    }

    /**
     * 自定义批量脱敏 可自定义脱敏内容、开始位置、脱敏长度、脱敏字符
     * @param textList 脱敏文本
     * @param start  开始位置
     * @param desensitizationLength  长度
     * @param maskChar 脱敏字符
     * @return
     */
    @NaslLogic
    public List<String> generalBulkDataMasking(List<String> textList, Integer start, Integer desensitizationLength, String maskChar) {
        return textList.stream()
                // 使用 map 操作将每个文本进行脱敏
                .map(text -> CustomDataMaskingUtil.customDesensitization(text, start - 1, desensitizationLength, maskChar))
                // 将脱敏后的文本收集到新的列表中
                .collect(Collectors.toList());
    }



}
