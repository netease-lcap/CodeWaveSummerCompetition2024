package com.netease;

import com.netease.lowcode.freemarker.dto.CreateDocxRequest;
import com.netease.lowcode.freemarker.util.FileUtil;
import com.netease.lowcode.freemarker.util.FreeMarkerUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
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
}
