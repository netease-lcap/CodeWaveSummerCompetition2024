import com.alibaba.fastjson2.JSON;
import com.netease.lowcode.pdf.extension.Excel2Pdf;
import com.netease.lowcode.pdf.extension.PdfGenerator;
import com.netease.lowcode.pdf.extension.structures.BaseResponse;
import com.netease.lowcode.pdf.extension.structures.CreateByXlsxRequest;
import com.netease.lowcode.pdf.extension.utils.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Excel2PdfTest {

    public static void main1(String[] args) {
        String s = "{\n" +
                "    \"name\":\"测试名字\",\n" +
                "    \"list\":[\n" +
                "        {\n" +
                "            \"no\":1,\n" +
                "            \"name\":\"项目1\",\n" +
                "            \"std\":\"国标1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"no\":2,\n" +
                "            \"name\":\"项目2\",\n" +
                "            \"std\":\"国标2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"no\":3,\n" +
                "            \"name\":\"项目3\",\n" +
                "            \"std\":\"国标3\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        CreateByXlsxRequest request = new CreateByXlsxRequest();
        request.setJsonData(s);
        request.setExportFileName("测试.pdf");
        request.setLastRowNum(27);
        request.setLastColLabel("H");
        request.setTemplateUrl("http://dev.erp.fx.lcap.163yun.com/upload/app/fa9c98b7-cc09-42bb-ba17-f65cc8bca381/导出pdf模板_20240828155248152.xlsx");
        BaseResponse baseResponse = Excel2Pdf.xlsx2pdf(request);
        System.out.println(baseResponse);
    }

    public static void main2(String[] args) throws IOException {
        File templateFile = new File("D:\\新建 XLSX 工作表.xlsx");

        String fileName = templateFile.getName();
        InputStream inputStream = new FileInputStream(templateFile);

        Workbook wb =new XSSFWorkbook(inputStream);
        Sheet sheet0 = wb.getSheetAt(0);
        Row row = sheet0.getRow(0);
        Cell cell0 = row.getCell(0);
        Cell cell1 = row.getCell(1);

        // 字体信息
        XSSFFont font0 = (XSSFFont) wb.getFontAt(cell0.getCellStyle().getFontIndexAsInt());
        XSSFFont font1 = (XSSFFont) wb.getFontAt(cell1.getCellStyle().getFontIndexAsInt());

        String fontName0 = font0.getFontName();
        String fontName1 = font1.getFontName();

        int family0 = font0.getFamily();
        int family1 = font1.getFamily();


        System.out.println();
    }

    public static void main3(String[] args) {
        CreateByXlsxRequest request = new CreateByXlsxRequest();

        Excel2Pdf.xlsx2pdf(request);
    }

    public static void main4(String[] args) {
        CreateByXlsxRequest request = new CreateByXlsxRequest();
        request.setTemplateUrl("http://dev.erp.fx.lcap.163yun.com/upload/app/fa9c98b7-cc09-42bb-ba17-f65cc8bca381/导出pdf模板_20240902145341687.xlsx");
        request.setJsonData("{\"nameB\":\"浙江来福我也不知道就乱天一通的地址我也不知道就乱天一通的地址谐波传动股份有限公司\",\"addressB\":\"我也不知道就乱天一我也不知道就乱天一通的地址我也不知道就乱天一通的地址我也不知道就乱天一通的地址我也不知道就乱天一通的地址通的地址\",\"contactorB\":\"孙照浩\",\"phoneB\":\"1530651530652604015306526040153065260401530652604026040\",\"emailB\":\"wb.sunao0nao0nao0nao0nao0nao0nao0nao0nao0nao0nao0nao01@mesg.corp.netease.com\",\"orderCode\":\"XZY240902038\",\"paymentTerms\":\"没有条件\",\"orderDate\":\"2024-09-02\",\"total\":161.9,\"list\":[{\"index\":1,\"mCode\":\"TEMP000000000000000000000000000000000000000000000000000000000000000000000000027\",\"mName\":\"【临时】测试PDF02\",\"mType\":null,\"count\":15,\"unitPrice\":6.8,\"totalPrice\":102,\"expectDate\":\"2024-08-30\"},{\"index\":2,\"mCode\":\"TEMP000026\",\"mName\":\"【临时】测试PDF01\",\"mType\":null,\"count\":10,\"unitPrice\":5.99,\"totalPrice\":59.9,\"expectDate\":\"2024-08-29\"}],\"nameA\":\"孙照浩\",\"phoneA\":\"15306526040\",\"emailA\":\"wb.sun01@mesg.corp.netease.com\"}");
        request.setLastRowNum(27);
        request.setLastColLabel("H");
        request.setExportFileName("导出订单【XZY240902038】.pdf");
        Excel2Pdf.xlsx2pdf(request);
    }

    public static void main5(String[] args) {
        String jsonData = "{\n" +
                "\t\"nameB\": \"浙江来福谐波传动股份有限公司\",\n" +
                "\t\"addressB\": \"我也不知道就乱天一通的地址我也不知道就乱天一通的地址我也不知道就乱天一通的地址我也不知道就乱天一通的地址我也不知道就乱天一通的地址\",\n" +
                "\t\"contactorB\": \"孙照浩\",\n" +
                "\t\"phoneB\": \"15306526015306526040153065260401530652604015306526040153065260401530652604040\",\n" +
                "\t\"emailB\": \"wb.sunzhaohao01@mesg.corp.netease.com\",\n" +
                "\t\"orderCode\": \"XZY240902038\",\n" +
                "\t\"paymentTerms\": \"没有条件\",\n" +
                "\t\"orderDate\": \"2024-09-02\",\n" +
                "\t\"total\": 161.9,\n" +
                "\t\"list\": [{\n" +
                "\t\t\"index\": 1,\n" +
                "\t\t\"mCode\": \"TEMP000027\",\n" +
                "\t\t\"mName\": \"【临时】测试PDF02\",\n" +
                "\t\t\"mType\": null,\n" +
                "\t\t\"count\": 15,\n" +
                "\t\t\"unitPrice\": 6.8,\n" +
                "\t\t\"totalPrice\": 102,\n" +
                "\t\t\"expectDate\": \"2024-08-30\"\n" +
                "\t}, {\n" +
                "\t\t\"index\": 2,\n" +
                "\t\t\"mCode\": \"TEMP000026\",\n" +
                "\t\t\"mName\": \"【临时】测试PDF01\",\n" +
                "\t\t\"mType\": null,\n" +
                "\t\t\"count\": 10,\n" +
                "\t\t\"unitPrice\": 5.99,\n" +
                "\t\t\"totalPrice\": 59.9,\n" +
                "\t\t\"expectDate\": \"2024-08-29\"\n" +
                "\t}],\n" +
                "\t\"nameA\": \"孙照浩\",\n" +
                "\t\"phoneA\": \"15306526040\",\n" +
                "\t\"emailA\": \"wb.sunzhaohao01@mesg.corp.netease.com\"\n" +
                "}";
        String template = "{\n" +
                "\t\"fileName\": \"导出订单【】.pdf\",\n" +
                "\t\"pageSize\": \"A4\",\n" +
                "\t\"rotate\": false,\n" +
                "\t\"nodes\": [{\n" +
                "\t\t\"type\": \"Table\",\n" +
                "\t\t\"width\": 100,\n" +
                "\t\t\"cells\": [{\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 18,\n" +
                "\t\t\t\t\"bold\": true,\n" +
                "\t\t\t\t\"text\": \"采 购 订 单\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 8,\n" +
                "\t\t\t\"width\": 100,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 88,\n" +
                "\t\t\t\t\t\"green\": 142,\n" +
                "\t\t\t\t\t\"blue\": 50\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 24,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"甲方：杭州网易轩之辕智能科技有限公司 \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"乙方：${nameB}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \" \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"地址：中国（浙江）自由贸易试验区杭州市滨江区长河街道网商路 399 号 3 幢 401 室 \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"地址：${addressB}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \" \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"联系人：${nameA}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"联系人：${contactorB}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \" \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"联系电话：${phoneA}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"联系电话：11111111111111111111111111111111111111111111111111111111111\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \" \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"邮箱：${emailA}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"邮箱：${emailB}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \" \"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"采购订单号：${orderCode}                             付款条件：${paymentTerms}                                   订购日期：${orderDate}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 8,\n" +
                "\t\t\t\"width\": 100,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"序号\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"物料编码\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"物料名称\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"物料类型\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"数量\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"单价(含税)\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"总额(含税)\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"需求时间\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"1\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"TEMP000027\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"【临时】测试PDF02\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"15\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"6.8\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"102\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"2024-08-30\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"2\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"TEMP000026\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"【临时】测试PDF01\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"10\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"5.99\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"59.9\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"2024-08-29\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 6,\n" +
                "\t\t\t\"width\": 75,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"总价(含税)\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"rgb\": {\n" +
                "\t\t\t\t\t\"red\": 0,\n" +
                "\t\t\t\t\t\"green\": 0,\n" +
                "\t\t\t\t\t\"blue\": 0\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"${total}\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"textAlignment\": \"CENTER\",\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"供应商需知: \\n1. 交货点详细地址： \\nA、网易轩之辕：浙江省杭州市滨江区网商路 399 号，网易二期园区；收件人：陈旭辉 15524338782。 \\nB、杭州中恒：富阳东洲街道大岭山路 358 号-4 号楼 1 楼 收料区；收件人：陈官华 15869163993。 \\n2. 供应商必须在所有外包装箱（袋）贴上（或打印）至少以下内容： 制造商名称/标志， 物料编码，内装数量，生产日期/批号，协议/订单号。 \\n3. 实际采购数量以订单为准， 除了经双方另有约定， 预测需求仅作为供应商备货， 备料准备生产能力时参考。 \\n4. 请仔细核对订单内容， 加强订单投产管理， 禁止遗漏/重复投产，否则后果自负。\\n5. 请供应商在每月 20 日前开具发票并提交至相应的采购， 采购以收到发票日期为准， 超过 23 日收到的发票自动顺延到下 个月再进行发票核销。 \\n6. 环保声明： 根据供需双方签署的 ROHS 协议， 若供方提供给需方时， 物料环保属性不符合欧盟 ROHS 指令， 供方将承担 由此给需方带来的一切损失。\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderTop\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"rowspan\": 8,\n" +
                "\t\t\t\"colspan\": 8,\n" +
                "\t\t\t\"width\": 100,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"轩之辕盖章：\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 3,\n" +
                "\t\t\t\"width\": 37,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 9,\n" +
                "\t\t\t\t\"bold\": false,\n" +
                "\t\t\t\t\"text\": \"供应商盖章：\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"rowspan\": 1,\n" +
                "\t\t\t\"colspan\": 2,\n" +
                "\t\t\t\"width\": 25,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 10,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderLeft\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\",\n" +
                "\t\t\t\t\"fontName\": \"等线\",\n" +
                "\t\t\t\t\"fontSize\": 12,\n" +
                "\t\t\t\t\"bold\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"borderBottom\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"borderRight\": {\n" +
                "\t\t\t\t\"width\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 12,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"elements\": [{\n" +
                "\t\t\t\t\"type\": \"Paragraph\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"width\": 13,\n" +
                "\t\t\t\"noBorder\": true\n" +
                "\t\t}],\n" +
                "\t\t\"columnSize\": 8\n" +
                "\t}]\n" +
                "}";
        PdfGenerator.createPDFV2ByStr(jsonData,template);
    }

    public static void main(String[] args) {

        D d = new D();
        d.list = new ArrayList<>();
        d.list.add(new E("test1","http://www.baidu.com"));
        d.list.add(new E("test2","https://www.taobao.com/"));

        CreateByXlsxRequest request = new CreateByXlsxRequest();
        request.setTemplateUrl("https://dev-excel2pdflinktest-kehfan.app.codewave.163.com:443/upload/?fileName=export_pdf_template_20241106.xlsx&fut=1731488570387&ai=5fcaeb4b-a695-4723-aef8-6a7190955a9d&con=lcap_default_connection");
        request.setLastRowNum(4);
        request.setLastColLabel("C");
        request.setExportFileName("测试测试22.pdf");
        request.setJsonData(JSON.toJSONString(d));
        BaseResponse baseResponse = Excel2Pdf.xlsx2pdf(request);
        System.out.println();
    }

    static class D {
        public String nameB="ss";
        public String emailA="aa";
        public String emailB="bb";
        public List<E> list;
    }

    static class E {

        public E() {

        }
        public E(String nName,String url) {
            this.mName = nName;
            this.url = url;
        }

        public String mCode = "code";
        public String totalPrice = "0.000";
        public String mName;
        public String url;
    }
}
