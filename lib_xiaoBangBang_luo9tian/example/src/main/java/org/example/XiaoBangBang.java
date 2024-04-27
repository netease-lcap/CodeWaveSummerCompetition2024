package org.example;

import com.google.gson.Gson;
import com.netease.lowcode.core.annotation.NaslConnector;
import org.example.customer.structure.*;
import org.example.order.structure.*;
import org.example.product.structure.*;
import org.example.productCategory.structure.*;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NaslConnector(connectorKind = "xiaoBangBang")
public class XiaoBangBang {
    private String token;
    public XiaoBangBang(){}
    @NaslConnector.Creator
    public XiaoBangBang initBean(String token){
        XiaoBangBang xbb=new XiaoBangBang();
        xbb.token=token;
        return xbb;
    }
    @NaslConnector.Tester
    public Boolean test(String token){
        if(token!=null){
            return true;
        }

        return false;
    }
/*
    获取与接口的链接，默认配置
*/
    public HttpURLConnection getCon (String path){
        HttpURLConnection con = null;
        try {
            URL url=new URL(path);
            con=(HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("charset","UTF-8");
            con.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }
/*
    进行sha-256加密获取sign值
*/
    @NaslConnector.Logic
    public String getSign(String data){
        String result="";
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("sha-256");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] digest=messageDigest.digest();
            result=new BigInteger(1,digest).toString(16).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
/*
    返回string类型的json接口数据再根据用户需求转相应类型
*/
    public String getTempResult(HttpURLConnection con,String data){
        String result="";
        try {
            OutputStreamWriter out=new OutputStreamWriter(con.getOutputStream(),"UTF-8");
            out.write(data);
            out.flush();
            InputStream is=con.getInputStream();
            BufferedReader in=new BufferedReader(new InputStreamReader(is));
            String str="";
            while((str=in.readLine())!=null){
                result+=str;
            }
            is.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
/*
    增加客户
*/
    @NaslConnector.Logic
    public AddCustomerVo addCustomer(AddCustomerDto data){
        AddCustomerVo result = null;
            /*
            将下面的部分抽出来
            */
            /*URL url=new URL("https://appapi.xbongbong.com/pro/v2/api/customer/add");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("charset","UTF-8");*/
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/customer/add");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(data.userId+data.corpid+data.dataList+data.formId)
        //newData=data.toString().replaceAll("[\\s| |\\n]","");
        //System.out.println(newData+data.zip());
        //获得往接口传数据和获得接口数据也抽出来了
            /*OutputStreamWriter out=new OutputStreamWriter(con.getOutputStream(),"UTF-8");
            System.out.println(g.toJson(data));
            out.write(g.toJson(data));
            //out.write(data.zip());
            out.flush();
            InputStream is=con.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String str="";
            String tempResult="";
            while((str=br.readLine())!=null){
                tempResult+=str;
            }*/
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, AddCustomerVo.class);
        //System.out.println("真正答案 "+result.msg);
        return result;
    }
    /*
    删除客户
*/
    @NaslConnector.Logic
    public DeleteCustomerVo deleteCustomer(DeleteCustomerDto data){
        DeleteCustomerVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/customer/del");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, DeleteCustomerVo.class);
        System.out.println("真正答案 "+result);
        con.disconnect();
        return result;
    }
/*
    修改客户
*/
    @NaslConnector.Logic
    public UpdateCustomerVo updateCustomer(UpdateCustomerDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/customer/edit");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String temp=getTempResult(con,g.toJson(data));
        UpdateCustomerVo result=g.fromJson(temp, UpdateCustomerVo.class);
        //System.out.println(result);
        return result;
    }
/*
    查询客户
*/
    @NaslConnector.Logic
    public SelectCustomerVo selectCustomer(SelectCustomerDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/customer/list");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String tempResult=getTempResult(con,g.toJson(data));
        SelectCustomerVo result=g.fromJson(tempResult, SelectCustomerVo.class);
        //System.out.println(result);
        return result;
    }
    /*
    增加产品
*/
    @NaslConnector.Logic
    public AddProductVo addProduct(AddProductDto data){
        AddProductVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/product/add");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(data.userId+data.corpid+data.dataList+data.formId)
        //newData=data.toString().replaceAll("[\\s| |\\n]","");
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, AddProductVo.class);
        //System.out.println("真正答案 "+result.msg);
        return result;
    }
    /*
    删除产品
*/
    @NaslConnector.Logic
    public DeleteProductVo deleteProduct(DeleteProductDto data){
        DeleteProductVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/product/del");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, DeleteProductVo.class);
        //System.out.println("真正答案 "+result);
        con.disconnect();
        return result;
    }
    /*
        修改产品
    */
    @NaslConnector.Logic
    public UpdateProductVo updateProduct(UpdateProductDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/product/edit");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String temp=getTempResult(con,g.toJson(data));
        UpdateProductVo result=g.fromJson(temp, UpdateProductVo.class);
        //System.out.println(result);
        return result;
    }
    /*
        查询产品
    */
    @NaslConnector.Logic
    public SelectProductVo selectProduct(SelectProductDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/product/list");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String tempResult=getTempResult(con,g.toJson(data));
        SelectProductVo result=g.fromJson(tempResult, SelectProductVo.class);
        //System.out.println(result);
        return result;
    }
    /*
   增加产品分类
*/
    @NaslConnector.Logic
    public AddProductCategoryVo addProductCategory(AddProductCategoryDto data){
        AddProductCategoryVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/product/categoryAdd");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(data.userId+data.corpid+data.dataList+data.formId)
        //newData=data.toString().replaceAll("[\\s| |\\n]","");
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, AddProductCategoryVo.class);
        //System.out.println("真正答案 "+result.msg);
        return result;
    }
    /*
    删除产品分类
*/
    @NaslConnector.Logic
    public DeleteProductCategoryVo deleteProductCategory(DeleteProductCategoryDto data){
        DeleteProductCategoryVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/product/categoryDel");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, DeleteProductCategoryVo.class);
        //System.out.println("真正答案 "+result);
        con.disconnect();
        return result;
    }
    /*
        修改产品分类
    */
    @NaslConnector.Logic
    public UpdateProductCategoryVo updateProductCategory(UpdateProductCategoryDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/product/categoryUpdate");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String temp=getTempResult(con,g.toJson(data));
        UpdateProductCategoryVo result=g.fromJson(temp, UpdateProductCategoryVo.class);
        //System.out.println(result);
        return result;
    }
    /*
        查询产品分类
    */
    @NaslConnector.Logic
    public SelectProductCategoryVo selectProductCategory(SelectProductCategoryDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/product/categoryList");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String tempResult=getTempResult(con,g.toJson(data));
        SelectProductCategoryVo result=g.fromJson(tempResult, SelectProductCategoryVo.class);
        //System.out.println(result);
        return result;
    }
    /*
  增加合同订单
*/
    @NaslConnector.Logic
    public AddOrderVo addProductOrder(AddOrderDto data){
        AddOrderVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/contract/add");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(data.userId+data.corpid+data.dataList+data.formId)
        //newData=data.toString().replaceAll("[\\s| |\\n]","");
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, AddOrderVo.class);
        //System.out.println("真正答案 "+result.msg);
        return result;
    }
    /*
    删除合同订单
*/
    @NaslConnector.Logic
    public DeleteOrderVo deleteOrder(DeleteOrderDto data){
        DeleteOrderVo result = null;
        HttpURLConnection con = getCon("https://appapi.xbongbong.com/pro/v2/api/contract/del");
        //String newData=data.zip()+token;
        Gson g = new Gson();
        //String sign=getSign(newData);
        String sign = getSign(g.toJson(data) + token);
        con.setRequestProperty("sign", sign);
        //System.out.println(sign);
        //System.out.println(newData+data.zip());
        String tempResult = getTempResult(con, g.toJson(data));
        result = g.fromJson(tempResult, DeleteOrderVo.class);
        //System.out.println("真正答案 "+result);
        con.disconnect();
        return result;
    }
    /*
        修改合同订单
    */
    @NaslConnector.Logic
    public UpdateOrderVo updateOrder(UpdateOrderDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/contract/edit");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String temp=getTempResult(con,g.toJson(data));
        UpdateOrderVo result=g.fromJson(temp, UpdateOrderVo.class);
        //System.out.println(result);
        return result;
    }
    /*
        查询合同订单
    */
    @NaslConnector.Logic
    public SelectOrderVo selectOrder(SelectOrderDto data){
        HttpURLConnection con=getCon("https://appapi.xbongbong.com/pro/v2/api/contract/list");
        Gson g=new Gson();
        String sign=getSign(g.toJson(data)+token);
        con.setRequestProperty("sign",sign);
        String tempResult=getTempResult(con,g.toJson(data));
        SelectOrderVo result=g.fromJson(tempResult, SelectOrderVo.class);
        //System.out.println(result);
        return result;
    }
    public static void main(String[] args) {
        XiaoBangBang xbb = new XiaoBangBang().initBean("");
        xbb.test("");
        System.out.println("联通完成");
    }
}
