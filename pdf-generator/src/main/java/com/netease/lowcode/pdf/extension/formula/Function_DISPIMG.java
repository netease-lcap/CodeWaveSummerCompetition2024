package com.netease.lowcode.pdf.extension.formula;

import com.netease.lowcode.pdf.extension.Excel2Pdf;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

import java.util.Base64;
import java.util.Map;

public class Function_DISPIMG implements FreeRefFunction {

    @Override
    public ValueEval evaluate(ValueEval[] valueEvals, OperationEvaluationContext operationEvaluationContext) {

        // _xlfn.DISPIMG("ID_9A6A2370D18A4DB59A864AF745EB04EF",1)
        Map<String, byte[]> sheetImages = Excel2Pdf.threadLocal.get();
        byte[] picData = sheetImages.get(((StringEval) valueEvals[0]).getStringValue());
        // 转base64编码？
        String s = Base64.getEncoder().encodeToString(picData);
        return new StringEval("base64,img," + s);
    }
}
