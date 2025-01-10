package com.netease.lowcode.pdf.extension.formula;

import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Workbook;

public class Register {

    public static void registerFormulas(Workbook wb){
        wb.addToolPack(new UDFFinder() {
            @Override
            public FreeRefFunction findFunction(String s) {
                switch (s){
                    case "_xlfn.DISPIMG":
                        return new Function_DISPIMG();
                    default:
                        return null;
                }
            }
        });
    }

}
