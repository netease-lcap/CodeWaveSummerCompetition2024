package com.netease.lowcode.custonapifilter.storage.db;

import com.netease.lowcode.annotation.helper.provider.OverriddenFrameworkHelper;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.custonapifilter.storage.StorageEnum;
import com.netease.lowcode.custonapifilter.storage.StorageService;
import com.netease.lowcode.custonapifilter.storage.redis.RedisConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis存储实现类
 */
@Component
public class dbStorageServiceImpl implements StorageService {
    private final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Resource
    private RedisConnector redisConnector;

    @Override
    public boolean checkAndAddIfAbsent(String key, Long timeout) {
        boolean flag = false;
        try {
            flag = (Boolean) OverriddenFrameworkHelper
                    .invokeOverriddenMethod0("checkAndAddIfAbsentByDb", "custon-api-filter", key);
//            flag = checkAndAddIfAbsentByDb(key);
        } catch (Exception e) {
            //可能是高并发时触发key的唯一索引
            log.error("db操作失败", e);
        }
        if (flag) {
            scheduler.schedule(() -> {
//                evictByDb(key)
                OverriddenFrameworkHelper
                        .invokeOverriddenMethod0("evictByDb", "custon-api-filter", key);
            }, timeout, TimeUnit.MILLISECONDS);
        }
        return flag;
    }

    @NaslLogic(override = true)
    public Boolean evictByDb(String key) {
        //todo 低代码复写逻辑
        return true;
    }

    @NaslLogic(override = true)
    public Boolean checkAndAddIfAbsentByDb(String key) {
        //todo 低代码复写逻辑
        return true;
    }

    @Override
    public String type() {
        return StorageEnum.DB.getType();
    }
}
