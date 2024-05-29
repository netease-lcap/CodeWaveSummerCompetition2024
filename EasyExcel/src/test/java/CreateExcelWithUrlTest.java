import com.netease.lowcode.extensions.CustomExcelData;
import com.netease.lowcode.extensions.CustomSheetData;
import com.netease.lowcode.extensions.EasyExcelTools;
import com.netease.lowcode.extensions.ExcelResponse;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateExcelWithUrlTest {

    public static void main(String[] args) {
        CustomExcelData excelData = new CustomExcelData();
        excelData.fileName = "test.xlsx";
        ArrayList<CustomSheetData> sheetList = new ArrayList<>();
        excelData.sheetList = sheetList;

        CustomSheetData sheet1 = new CustomSheetData();
        sheetList.add(sheet1);
        CustomSheetData sheet2 = new CustomSheetData();
        sheetList.add(sheet2);

        sheet1.sheetName = "测试sheet1";
        sheet2.sheetName = "测试sheet2";

        sheet1.head = new ArrayList<>();
        sheet1.head.add("表头1");
        sheet2.head = new ArrayList<>();
        sheet2.head.add("表头2");

        sheet1.data = new ArrayList<>();
        sheet1.data.add(Arrays.asList("测试1"));
        sheet1.data.add(Arrays.asList("测试1"));
        sheet1.data.add(Arrays.asList("测试1"));
        sheet2.data = new ArrayList<>();
        sheet2.data.add(Arrays.asList("测试2"));
        sheet2.data.add(Arrays.asList("测试2"));
        sheet2.data.add(Arrays.asList("测试2"));
        sheet2.data.add(Arrays.asList("测试2"));

        ExcelResponse excelWithUrl = EasyExcelTools.createExcelWithUrl(excelData);
        System.out.println();
    }

}
