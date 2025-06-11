package com.netease.lowcode.lib.process.command;

import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.CommentEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.task.Comment;

import java.util.Date;

public class ExtAddProcessRecordCmd implements Command<Comment> {
    public static final String COMMENT_TYPE = "process_record_noshow";
    protected String processInstanceId;
    protected String taskId;
    protected String userName;
    protected Date recordCreateTime;
    protected String action;
    protected String message;
    protected String fullMessage;


    public ExtAddProcessRecordCmd(String processInstanceId, String taskId, String userName, Date recordCreateTime,
                                  String action, String message, String fullMessage) {
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.userName = userName;
        this.recordCreateTime = recordCreateTime;
        this.action = action;
        this.message = message;
        this.fullMessage = fullMessage;
    }

    @Override
    public Comment execute(CommandContext commandContext) {
        CommentEntity comment = CommandContextUtil.getCommentEntityManager(commandContext).create();
        comment.setUserId(userName);
        comment.setType(COMMENT_TYPE);
        comment.setTime(recordCreateTime);
        comment.setTaskId(taskId);
        comment.setProcessInstanceId(processInstanceId);
        comment.setAction(action);
        comment.setMessage(message);
        comment.setFullMessage(fullMessage);
        CommandContextUtil.getCommentEntityManager(commandContext).insert(comment);
        return comment;
    }
}
