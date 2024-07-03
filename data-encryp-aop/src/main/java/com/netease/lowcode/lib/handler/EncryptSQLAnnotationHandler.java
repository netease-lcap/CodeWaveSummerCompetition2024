package com.netease.lowcode.lib.handler;

import com.netease.lowcode.annotation.LCAPAnnotation;
import com.netease.lowcode.annotation.context.EntityContext;
import com.netease.lowcode.annotation.exception.LCAPAnnotationSQLHandlerException;
import com.netease.lowcode.annotation.handler.LCAPSQLAnnotationHandler;
import com.netease.lowcode.annotation.javabean.BuildAssignmentArgs;
import com.netease.lowcode.annotation.javabean.BuildSelectExprArgs;
import com.netease.lowcode.annotation.javabean.BuildWhereConditionArgs;
import com.netease.lowcode.annotation.javabean.IsFilterableArgs;
import com.netease.lowcode.lib.annotation.EncryptEntityAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class EncryptSQLAnnotationHandler implements LCAPSQLAnnotationHandler<EncryptEntityAnnotation> {
    private static final Logger log = LoggerFactory.getLogger(EncryptSQLAnnotationHandler.class);

    /**
     * 上报方法   // 详见案例1
     */
    @Override
    public Boolean report(List<EntityContext<EncryptEntityAnnotation>> list) throws LCAPAnnotationSQLHandlerException {
        return true;// 如果不需要使用"上报"机制，直接return即可
    }

    @Override
    public Map<String, Object> listParamValues(String s, EntityContext<EncryptEntityAnnotation> entityContext) {
        return null;
    }

    @Override
    public String buildWhereCondition(BuildWhereConditionArgs buildWhereConditionArgs, EntityContext<EncryptEntityAnnotation> entityContext) throws LCAPAnnotationSQLHandlerException {
        return null;
    }

    @Override
    public String buildSelectExpr(BuildSelectExprArgs buildSelectExprArgs, EntityContext<EncryptEntityAnnotation> entityContext) {
        // 从entityContext中获取注解的值。entityContext是IDE中打开EncryptEntityAnnotation注解的实体的上下文对象，包括注解的值、实体的信息等
        String encryptFieldList = entityContext.getAnnotation().encryptFieldList;
        String[] encryptFields = encryptFieldList.split(",");
        for (String encryptField : encryptFields) {
            // buildSelectExprArgs example：
            // 可以看到prevSelectExpr携带别名
            // {"entityName":"defaultDS.Entity1","propertyName":"id","prevSelectExpr":"`entity1`.`id` `T1_C1`"}
            if (encryptField.equals(buildSelectExprArgs.getPropertyName())) {
                //用空格分割 buildSelectExprArgs.getPrevSelectExpr()。兼容有别名的情况
                if (!StringUtils.isEmpty(buildSelectExprArgs.getPrevSelectExpr())) {
                    String[] prevSelectExpr = buildSelectExprArgs.getPrevSelectExpr().split(" ");
                    String md5Expr = null;
                    if (prevSelectExpr.length == 1) {
                        md5Expr = "md5(" + buildSelectExprArgs.getPrevSelectExpr() + ") " + encryptField;
                    } else if (prevSelectExpr.length == 2) {
                        md5Expr = " md5(" + prevSelectExpr[0] + ") " + prevSelectExpr[1];
                    }
                    return md5Expr;
                }
            }
        }
        return buildSelectExprArgs.getPrevSelectExpr();
    }

    @Override
    public boolean isFilterable(IsFilterableArgs isFilterableArgs, EntityContext<EncryptEntityAnnotation> entityContext) throws LCAPAnnotationSQLHandlerException {
        return false;
    }

    @Override
    public String buildAssignment(BuildAssignmentArgs buildAssignmentArgs, EntityContext<EncryptEntityAnnotation> entityContext) throws LCAPAnnotationSQLHandlerException {
        return null;
    }

    /**
     * 绑定的注解
     */
    @Override
    public Class<? extends LCAPAnnotation> consume() {
        return EncryptEntityAnnotation.class; // 由于这是IpLogicAnnotation的处理器，所以此处也必须返回它
    }

    /**
     * 执行顺序，order越小，这个对象的handle在同一advise时机下，相比别的handler越先执行。
     * order相同时不保证执行顺序
     * 缺省为0
     */
    @Override
    public Integer order() {
        return LCAPSQLAnnotationHandler.super.order();
    }
}
