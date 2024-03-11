package com.fdddf.IDUtil;

import com.netease.lowcode.core.annotation.NaslStructure;

/**
 * YitIdGeneratorOptions
 */
@NaslStructure
public class YitIdGeneratorOptions {
    /**
     * 机器码
     * 必须由外部设定，最大值 2^WorkerIdBitLength-1
     */
    public String WorkerId = "0";

    /**
     * 机器码位长
     * 默认值6，取值范围 [1, 15]（要求：序列数位长+机器码位长不超过22）
     */
    public String WorkerIdBitLength = "6";

    /**
     * 序列数位长
     * 默认值6，取值范围 [3, 21]（要求：序列数位长+机器码位长不超过22）
     */
    public String SeqBitLength = "6";

    /**
     * 最大序列数（含）
     * 设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，表示最大序列数取最大值（2^SeqBitLength-1]）
     */
    public String MaxSeqNumber = "0";

    /**
     * 最小序列数（含）
     * 默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号是0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位
     */
    public String MinSeqNumber = "5";

    /**
     * 最大漂移次数（含）
     * 默认2000，推荐范围500-10000（与计算能力有关）
     */
    public String TopOverCostCount = "2000";

}
