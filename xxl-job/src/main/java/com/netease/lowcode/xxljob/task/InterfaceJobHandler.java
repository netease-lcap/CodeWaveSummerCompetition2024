package com.netease.lowcode.xxljob.task;

import com.netease.lowcode.xxljob.model.JobHandlerInterfaceModel;
import com.netease.lowcode.xxljob.util.SpringContextUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于接口的JobHandler基类
 */
public abstract class InterfaceJobHandler extends IJobHandler {

    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    protected final ApplicationContext applicationContext;
    private List<JobHandlerInterfaceModel> jobModelList;

    public InterfaceJobHandler(ApplicationContext applicationContext, List<JobHandlerInterfaceModel> jobModelList) {
        this.applicationContext = applicationContext;
        this.jobModelList = jobModelList;
    }

    /**
     * 获取处理器名称
     */
    public abstract String getHandlerName();

    @Override
    public void execute() {
        String threadName = Thread.currentThread().getName();
        threadName = threadName.replace(",", "").replace(" ", "");
        Thread.currentThread().setName(threadName);
        String param = XxlJobHelper.getJobParam();
        String handlerName = getHandlerName();
        XxlJobHelper.log("任务开始执行，handler=" + getHandlerName() + ", 参数=" + param);
        log.info("xxl job execute = {}", handlerName);
        List<JobHandlerInterfaceModel> modelList = jobModelList.stream()
                .filter(e -> e.getJobHandler().equals(handlerName))
                .collect(Collectors.toList());
        // 一个job handler可能在多个接口上
        for (JobHandlerInterfaceModel model : modelList) {
            Object bean = SpringContextUtil.getCustomServiceBean(model.getLogicName());
            if (bean == null) {
                log.error("Failed to get bean: {}", model.getLogicName());
                XxlJobHelper.log("获取bean失败: " + model.getLogicName());
                XxlJobHelper.handleFail("获取bean失败: " + model.getLogicName());
                return;
            }
            Class<?> aClass = AopUtils.getTargetClass(bean);
            try {
                // 无参
                Method targetMethod = aClass.getDeclaredMethod(model.getLogicName());
                targetMethod.invoke(bean);
            } catch (NoSuchMethodException exception) {
                try {
                    Method targetMethod = aClass.getDeclaredMethod(model.getLogicName(), String.class);
                    targetMethod.invoke(bean, param);
                } catch (Exception e) {
                    log.error("Execute job failed: " + handlerName, ((InvocationTargetException) e).getTargetException().getMessage());
                    XxlJobHelper.log("任务执行失败: " + ((InvocationTargetException) e).getTargetException().getMessage());
                    XxlJobHelper.handleFail("任务执行失败: " + ((InvocationTargetException) e).getTargetException().getMessage());
                    return;
                }
            } catch (Exception e) {
                log.error("Execute job failed: " + handlerName, e);
                XxlJobHelper.log("任务执行失败: " + ((InvocationTargetException) e).getTargetException().getMessage());
                XxlJobHelper.handleFail("任务执行失败: " + ((InvocationTargetException) e).getTargetException().getMessage());
                return;
            }
        }
        XxlJobHelper.handleSuccess("任务执行成功");
    }
}