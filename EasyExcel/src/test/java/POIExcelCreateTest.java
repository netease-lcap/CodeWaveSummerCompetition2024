import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.lowcode.extensions.JsonUtil;
import com.netease.lowcode.extensions.easyexcel.poi.POIExcelCreate;
import com.netease.lowcode.extensions.easyexcel.poi.POIExcelCreateDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class POIExcelCreateTest {

    public static void main(String[] args) throws JsonProcessingException {
        POIExcelCreateDTO request = new POIExcelCreateDTO();
        request.setExportFileName("tttttttt1.xls");

        List<DataStructure> data = new ArrayList<>();
        DataStructure e = new DataStructure();
        e.aBoolean = true;
        e.aLong = 1234545L;
        e.string = "fdfsdf";
        e.localTime = LocalTime.now();
        e.localDate = LocalDate.now();
        e.zonedDateTime = ZonedDateTime.now();
        e.bigDecimal = new BigDecimal("3.35");
        e.list = Arrays.asList("3","4");
        e.map = new HashMap<String, String>() {
            {
                put("a", "b");
                put("b", "b");
                put("c", "b");
            }
        };
        e.num = 10L;
        data.add(e);

        for (int i = 0; i < 10; i++) {
            DataStructure e1 = new DataStructure();
            e1.num  = RandomUtil.randomLong(1,100);
            e1.aBoolean = true;
            e1.aLong = 1234545L;
            e1.string = "fdfsdf";
            e1.localTime = LocalTime.now();
            e1.localDate = LocalDate.now();
            e1.zonedDateTime = ZonedDateTime.now();
            e1.bigDecimal = new BigDecimal("3.35");
            e1.list = Arrays.asList("3","4");
            e1.map = new HashMap<String, String>() {
                {
                    put("a", "b");
                    put("b", "b");
                    put("c", "b");
                }
            };
            data.add(e1);
        }

        request.setJsonData(JsonUtil.toJson(data));
        POIExcelCreate.poiCreateXls(request,DataStructure.class);
    }
}

class DataStructure {

    @Label("@Style={\"colWidth\":20,\"title\":\"boolean测试表头\"}")
    public Boolean aBoolean;
    @Label("long测试表头")
    public Long aLong;
    @Label("string")
    public String string;
    @Label("localtime")
    public LocalTime localTime;
    @Label("localDate")
    public LocalDate localDate;
    @Label("zonedDateTime")
    public ZonedDateTime zonedDateTime;
    @Label("bigDecimal")
    public BigDecimal bigDecimal;
    @Label("list")
    public List<String> list;
    @Label("map")
    public Map<String,String> map;

    @Label("@Style={\"rowBackgroundCondition\":\"GREEN<20:RED<BLACK\",\"title\":\"测试标题\",\"index\":9}")
    public Long num;
}
