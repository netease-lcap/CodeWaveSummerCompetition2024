package com.yu.dto;

import com.netease.lowcode.core.annotation.NaslStructure;
import lombok.ToString;

import java.util.List;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/19 17:17
 **/
@ToString
@NaslStructure
public class QuerySendResultDto {
    /**
     * 无效的用户id
     */
    public List<String> invalid_user_id_list;
    /**
     * 禁止发送的用户id
     */
    public List<String> forbidden_user_id_list;
    /**
     * 发送失败的用户id
     */
    public List<String> failed_user_id_list;
    /**
     * 已读的用户id
     */
    public List<String> read_user_id_list;
    /**
     * 未读的用户id
     */
    public List<String> unread_user_id_list;
    /**
     * 无效的部门id
     */
    public List<Long> invalid_dept_id_list;
    public SendMsgResultDto forbidden_list;

}
