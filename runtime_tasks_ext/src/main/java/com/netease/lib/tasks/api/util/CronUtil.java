package com.netease.lib.tasks.api.util;

import org.springframework.scheduling.support.CronSequenceGenerator;

public class CronUtil {
    public static Boolean checkCron(String cronExpressionString) {
        try {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpressionString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
