package com.netease.lib.redistemplatetool.annotation;

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
public class RedissonAnnotationHandler implements LCAPLogicAnnotationHandler<RedissonLogicAnnotation> {

    @Override
    public LCAPAnnotationHandlerAdvise[] advises() {
        return new LCAPAnnotationHandlerAdvise[]{LCAPAnnotationHandlerAdvise.BEFORE, LCAPAnnotationHandlerAdvise.AFTER};
    }

    @Override
    public Class<? extends LCAPAnnotation> consume() {
        return RedissonLogicAnnotation.class;
    }

    @Override
    public Boolean report(List<LogicContext<RedissonLogicAnnotation>> logicContexts) {
        return true;
    }

    @Override
    public Object handle(Object[] args, Object result, LogicContext<RedissonLogicAnnotation> context) {
        return null;
    }
}
