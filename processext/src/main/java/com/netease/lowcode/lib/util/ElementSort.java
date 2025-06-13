package com.netease.lowcode.lib.util;

import com.netease.codewave.process.api.domain.graph.CWElementModel;
import com.netease.codewave.process.api.domain.graph.CWFlowModel;

import java.util.*;

public class ElementSort {
    public static List<CWElementModel> sortElements(
            List<CWElementModel> elements,
            List<CWFlowModel> flows) {
        Map<String, CWElementModel> nameToElement = new HashMap<>();
        for (CWElementModel e : elements) nameToElement.put(e.getName(), e);

        Map<String, Set<String>> graph = new LinkedHashMap<>(); // 保持顺序
        Map<String, Integer> inDegree = new HashMap<>();
        for (CWElementModel e : elements) {
            if (!graph.containsKey(e.getName())) graph.put(e.getName(), new LinkedHashSet<>());
            inDegree.put(e.getName(), 0);
        }
        for (CWFlowModel flow : flows) {
            graph.get(flow.getSourceRef()).add(flow.getTargetRef());
            inDegree.put(flow.getTargetRef(), inDegree.getOrDefault(flow.getTargetRef(), 0) + 1);
        }

        // 识别所有网关
        Set<String> gateways = new HashSet<>();
        for (CWElementModel e : elements) {
            if ("InclusiveGateway".equals(e.getType()) || "ExclusiveGateway".equals(e.getType())) {
                gateways.add(e.getName());
            }
        }

        // 去除网关，保留上下游关系
        for (String gateway : gateways) {
            Set<String> preNodes = new HashSet<>();
            Set<String> nextNodes = new HashSet<>();
            for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
                if (entry.getValue().contains(gateway)) preNodes.add(entry.getKey());
            }
            nextNodes.addAll(graph.get(gateway));
            for (String pre : preNodes) {
                for (String next : nextNodes) {
                    if (!gateways.contains(pre) && !gateways.contains(next)) {
                        graph.get(pre).add(next);
                        inDegree.put(next, inDegree.get(next) + 1);
                    }
                }
            }
        }
        // 移除网关节点
        for (String gateway : gateways) {
            graph.remove(gateway);
            inDegree.remove(gateway);
            for (Set<String> set : graph.values()) set.remove(gateway);
        }

        // 更新入度
        for (String name : inDegree.keySet()) inDegree.put(name, 0);
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Kahn算法拓扑排序
        List<CWElementModel> result = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> processed = new HashSet<>();

        for (String node : graph.keySet()) {
            if (inDegree.get(node) == 0) queue.add(node);
        }

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            if (!processed.contains(curr)) {
                result.add(nameToElement.get(curr));
                processed.add(curr);
            }
            if (graph.get(curr) != null) {
                for (String next : graph.get(curr)) {
                    inDegree.put(next, inDegree.get(next) - 1);
                    if (inDegree.get(next) == 0) queue.add(next);
                }
            }
        }

        // 检查是否有未处理的节点（环），按原顺序追加
        for (String name : graph.keySet()) {
            if (!processed.contains(name)) {
                result.add(nameToElement.get(name));
                processed.add(name);
            }
        }

        return result;
    }

}

