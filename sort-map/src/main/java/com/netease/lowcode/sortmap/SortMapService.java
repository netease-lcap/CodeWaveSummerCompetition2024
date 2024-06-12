package com.netease.lowcode.sortmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.sortmap.structure.SortStructure;

import java.io.IOException;
import java.util.*;

/**
 * @author xujianping
 * @date 2023/12/26 11:00 上午
 */
public class SortMapService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 有序的map
     *
     * @param structureList
     * @return
     */
    @NaslLogic
    public static Map<String, String> sortMap(List<SortStructure> structureList) {
        if (structureList == null || structureList.isEmpty()) {
            return new LinkedHashMap<>();
        }

        Map<String, String> map = new LinkedHashMap<>();
        structureList.stream().sorted(Comparator.comparingInt(e -> e.index)).forEach(e -> {
            map.put(e.getKey(), e.getValue());
        });
        return map;
    }

    /**
     * 有序的json
     *
     * @param jsonStr  需要转化的json
     * @param template 模板json，定义排列顺序
     * @return 按照模板排序的json
     * @throws IOException
     */
    @NaslLogic
    public static String sortJson(String jsonStr, String template) throws IOException {
        JsonNode templateNode = objectMapper.readTree(template);
        JsonNode jsonStrNode = objectMapper.readTree(jsonStr);
        traverseJsonNode(jsonStrNode, templateNode);
        return templateNode.toString();
    }

    private static void traverseJsonNode(JsonNode jsonStrNode, JsonNode templateNode) {
        if (templateNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = templateNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getValue().isValueNode() || (entry.getValue().isArray() && entry.getValue().get(0).isValueNode())) {
                    replaceValueNode((ObjectNode) templateNode, entry.getKey(), jsonStrNode.get(entry.getKey()));
                } else {
                    traverseJsonNode(jsonStrNode.get(entry.getKey()), entry.getValue());
                }
            }
        } else if (templateNode.isArray()) {
            for (int i = 0; i < templateNode.size(); i++) {
                traverseJsonNode(jsonStrNode.get(i), templateNode.get(i));
            }
        }
    }

    private static void replaceValueNode(ObjectNode templateNode, String key, JsonNode value) {
        templateNode.replace(key, value);
    }
}
