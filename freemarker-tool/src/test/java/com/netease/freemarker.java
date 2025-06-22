package com.netease;

import com.netease.lowcode.freemarker.dto.CreateDocxRequest;
import com.netease.lowcode.freemarker.dto.CreateRequest;
import com.netease.lowcode.freemarker.util.FileUtil;
import com.netease.lowcode.freemarker.util.FreeMarkerUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class freemarker {
    @Test
    public void testCreateFile() {
        System.out.println("freemarker");
    }
    @Test
    public void testCreateDocx() {
        CreateDocxRequest createDocxRequest = new CreateDocxRequest();
        createDocxRequest.templateUrl=new HashMap<String,String>(){{
           put("word/document.xml","file:///C:/Users/liuyuqi07/Documents/%E6%88%91%E7%9A%84POPO/document.xml");
           put("word/_rels/document.xml.rels","file:///C:/Users/liuyuqi07/Documents/%E6%88%91%E7%9A%84POPO/modal1/word/_rels/document.xml.rels");
           put("word/theme/theme1.xml","file:///C:/Users/liuyuqi07/Documents/%E6%88%91%E7%9A%84POPO/modal1/word/theme/theme1.xml");
        }};
        createDocxRequest.jsonData="{\"projectName\":\"南通重装2025境外年度决算审计\",\"dw\":\"南通重装\",\"st\":\"2025-02-12\",\"sy\":\"2025\",\"uList\":[{\"name\":\"小明\",\"role\":\"组长\"}]}";
        createDocxRequest.outFileName="test.docx";
        createDocxRequest.templateDocxFileUrl="file:///C:/Users/liuyuqi07/Documents/%E6%88%91%E7%9A%84POPO/modal1.docx";
        FreeMarkerUtil.createNewDocxFile(createDocxRequest);
    }
    @Test
    public void testCreateDocx2() throws UnsupportedEncodingException {
        String trueUrl = FileUtil.getTrueUrl("https://dev-pdf-jystudy2.app.codewave.163.com/upload/你好.pdf?fileName=你好.pdf&fut=1738828190217&ai=6fa0663f-4579-4784-a644-6a031200465b&con=lcap_default_connection&fpp=/app");
        System.out.println(trueUrl);
        trueUrl = FileUtil.getTrueUrl("https://dev-pdf-jystudy2.app.codewave.163.com/upload/你好.pdf");
        System.out.println(trueUrl);
        trueUrl = FileUtil.getTrueUrl("/upload/你好.pdf?fileName=你好.pdf&fut=1738828190217&ai=6fa0663f-4579-4784-a644-6a031200465b&con=lcap_default_connection&fpp=/app");
        System.out.println(trueUrl);

    }

    @Test
    public void testCreateNewXlsx() {
        try {
            CreateRequest createRequest = new CreateRequest();
//            createRequest.templateUrl = "http://dev.demozpl.defaulttenant.lcap.v3112a-lowcode.com/upload/?fileName=集成计划问题模板.xml&fut=1749094973365&ai=39956a50-2622-44d3-bb61-5ba897dc2c68&con=lcap_default_connection";
            createRequest.templateUrl = "http://dev.demo002.defaulttenant.lcap.v3133b-lowcode.com/upload/集成计划问题模板.xml?fileName=集成计划问题模板.xml&fut=1749473604370&ai=655bbd3c-772e-48bb-8d34-6c579b77b704&con=lcap_default_connection&fpp=/app";
            createRequest.jsonData = new String(Files.readAllBytes(Paths.get("/Users/zhangpenglan/Documents/projects/project/CodeWaveSummerCompetition2024/freemarker-tool/src/test/java/com/netease/demo.txt")));
            createRequest.outFileName = "zpldemo.xlsx";
            FreeMarkerUtil.createNewXlsx(createRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateNewXlsxV2() {
        try {
            CreateRequest createRequest = new CreateRequest();
            createRequest.templateUrl = "http://dev.demo002.defaulttenant.lcap.v3133b-lowcode.com/upload/aa.xml?fileName=aa.xml&fut=1749544386313&ai=655bbd3c-772e-48bb-8d34-6c579b77b704&con=lcap_default_connection&fpp=/app";
            createRequest.jsonData = new String(Files.readAllBytes(Paths.get("/Users/zhangpenglan/Documents/projects/project/CodeWaveSummerCompetition2024/freemarker-tool/src/test/java/com/netease/demo.txt")));
            createRequest.outFileName = "zpldemo.xlsx";
            FreeMarkerUtil.createNewXlsx(createRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
