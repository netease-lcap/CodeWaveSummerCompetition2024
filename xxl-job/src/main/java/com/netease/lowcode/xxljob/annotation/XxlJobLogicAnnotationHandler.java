package com.netease.lowcode.xxljob.annotation;

import com.netease.lowcode.annotation.LCAPAnnotation;
import com.netease.lowcode.annotation.context.LogicContext;
import com.netease.lowcode.annotation.handler.LCAPAnnotationHandlerAdvise;
import com.netease.lowcode.annotation.handler.LCAPLogicAnnotationHandler;

import java.util.List;

/**
 * 防重放逻辑处理器
 *
 * @author xujianping
 */
public class XxlJobLogicAnnotationHandler implements LCAPLogicAnnotationHandler<XxlJobLogicAnnotation> {

    @Override
    public LCAPAnnotationHandlerAdvise[] advises() {
        return new LCAPAnnotationHandlerAdvise[]{LCAPAnnotationHandlerAdvise.BEFORE};
    }

    @Override
    public Class<? extends LCAPAnnotation> consume() {
        return XxlJobLogicAnnotation.class;
    }

    @Override
    public Boolean report(List<LogicContext<XxlJobLogicAnnotation>> logicContexts) {
        return true;
    }

    @Override
    public Object handle(Object[] args, Object result, LogicContext<XxlJobLogicAnnotation> context) {
        return null;
    }
}
