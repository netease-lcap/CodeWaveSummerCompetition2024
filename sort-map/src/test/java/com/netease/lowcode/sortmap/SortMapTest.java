package com.netease.lowcode.sortmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netease.lowcode.sortmap.structure.SortStructure;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SortMapTest {

    /**
     * 测试json有序
     * @throws Exception
     */
//    @Test
    public void testSortJson() throws Exception {
        String template;
        try (InputStream inputStream = SortMapTest.class.getClassLoader().getResourceAsStream("template.json");
             Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")) {
            template = scanner.hasNext() ? scanner.next() : "";
        }
        try (InputStream inputStream = SortMapTest.class.getClassLoader().getResourceAsStream("jsonStr.json");
             Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")) {
            String jsonStr = scanner.hasNext() ? scanner.next() : "";
            String result = SortMapService.sortJson(jsonStr, template);
            System.out.println(result);
        }
    }

    /**
     * 测试有序的map
     * @throws JsonProcessingException
     */
//    @Test
    public void yourTestMethod() throws JsonProcessingException {
        SortStructure sortStructure = new SortStructure();
        sortStructure.setIndex(1);
        sortStructure.setKey("adad");
        sortStructure.setValue("adad");

        SortStructure sortStructure1 = new SortStructure();
        sortStructure1.setIndex(0);
        sortStructure1.setKey("123");
        sortStructure1.setValue("123");

        SortStructure sortStructure4 = new SortStructure();
        sortStructure4.setIndex(3);
        sortStructure4.setKey("1234");
        sortStructure4.setValue("1234");

        List<SortStructure> structureList = new ArrayList<>();
        structureList.add(sortStructure);
        structureList.add(sortStructure1);
        structureList.add(sortStructure4);
        Map<String, String> map1 = SortMapService.sortMap(structureList);
        System.out.println(map1);
    }
}
