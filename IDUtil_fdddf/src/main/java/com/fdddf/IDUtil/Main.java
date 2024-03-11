package com.fdddf.IDUtil;

import com.fdddf.IDUtil.api.IDUtil;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println(IDUtil.UUID());
        System.out.println(IDUtil.UUIDSimple());
        System.out.println(IDUtil.isValidUUID(IDUtil.UUID()));

        SnowflakeOptions options = new SnowflakeOptions();
        options.datacenterId = "0";
        options.workerId = "0";
        System.out.println(IDUtil.SnowflakeId(options));
        System.out.println(IDUtil.HexTimeId());
        IDUtilTime t = new IDUtilTime();
        t.startTime = "2024-03-01 00:00:00";
        t.randomLength = "0";
        System.out.println(IDUtil.TimeId(t));
        t.randomLength = "5";
        System.out.println(IDUtil.TimeId(t));
        System.out.println(IDUtil.YitId(new YitIdGeneratorOptions()));
    }
}
