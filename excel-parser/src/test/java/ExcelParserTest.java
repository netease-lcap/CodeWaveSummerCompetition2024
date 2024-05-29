import com.netease.lowcode.extension.excel.ExcelParser;

public class ExcelParserTest {


    public static void main(String[] args) {
        String url = "http://xxx/库存表2024012502543737_20240126095330793.xls";
        ExcelParser.parseAllSheet(url,null,null,null,Entity2.class);
    }

}
