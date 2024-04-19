package com.fdddf.ocrinvoice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fdddf.ocrinvoice.rollticket.RollTicketInvoice;

public class Main {
    public static void main(String[] args) {
        String jsonString = "{\"data\":{\"发票代码\":\"041001800107\",\"发票号码\":\"04594258\",\"开票日期\":\"2019年4月27日\",\"校验码\":\"65584298472538424744\",\"销售方名称\":\"中国石化销售有限公司河南许昌石油分公司第三经营部\",\"销售方税号\":\"914110237241033719\",\"购买方名称\":\"上海恒企教育培训有限公司许昌许都广场分公司\",\"购买方税号\":\"91411000MA456QT38M\",\"总价\":\"￥600.00\"},\"angle\":0,\"height\":2192,\"orgHeight\":2139,\"orgWidth\":3116,\"width\":3152,\"sid\":\"bfcb418f71fd057f11ea4e17b15688dc27f9d4c6a47a867a376f251706266353341da54c\"}";

        RollTicketInvoice invoice = JSONObject.toJavaObject(JSON.parseObject(jsonString), RollTicketInvoice.class);

        System.out.println("Invoice: " + invoice.toString());

        System.out.println("Invoice Code: " + invoice.data.invoiceCode);
        System.out.println("Invoice Number: " + invoice.data.invoiceNumber);
        System.out.println("Invoice Date: " + invoice.data.invoiceDate);
        System.out.println("Seller Name: " + invoice.data.sellerName);
        System.out.println("Buyer Name: " + invoice.data.buyerName);
        System.out.println("Total Price: " + invoice.data.totalPrice);
    }
}
