package com.netease.lib.redistemplatetool.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleRoundRobinAssigner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 设置候选列表（自动处理列表变化）
     */
    @NaslLogic
    public Boolean pollingSetCandidates(String nodeId, List<String> candidates) {
        String candidateKey = "node:" + nodeId + ":candidates";
        // 删除旧列表，设置新列表
        redisTemplate.delete(candidateKey);
        if (!candidates.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(candidateKey, candidates);
        }
        return true;
    }

    /**
     * 选择下一个候选
     */
    @NaslLogic
    public String pollingSelectNext(String nodeId) {
        String candidateKey = "node:" + nodeId + ":candidates";
        String lastKey = "node:" + nodeId + ":last";

        // 获取当前列表
        List<String> candidates = redisTemplate.opsForList().range(candidateKey, 0, -1);
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("没有可用的候选人");
        }

        // 获取上次分配的人
        String last = redisTemplate.opsForValue().get(lastKey);

        // 找到下一个候选人
        String next = findNextCandidate(candidates, last);

        // 记录这次分配的人
        redisTemplate.opsForValue().set(lastKey, next);

        return next;
    }

    /**
     * 简单的轮询查找逻辑
     */
    private String findNextCandidate(List<String> candidates, String last) {
        if (last == null || last.isEmpty()) {
            return candidates.get(0); // 第一次从第一个开始
        }

        // 找上次分配的人在列表中的位置
        int lastIndex = -1;
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).equals(last)) {
                lastIndex = i;
                break;
            }
        }

        // 如果没找到或者是在最后，就从第一个开始
        if (lastIndex == -1 || lastIndex == candidates.size() - 1) {
            return candidates.get(0);
        }

        // 否则选下一个
        return candidates.get(lastIndex + 1);
    }

    /**
     * 获取当前候选人列表
     */
    public List<String> getCandidates(String nodeId) {
        String candidateKey = "node:" + nodeId + ":candidates";
        return redisTemplate.opsForList().range(candidateKey, 0, -1);
    }
}