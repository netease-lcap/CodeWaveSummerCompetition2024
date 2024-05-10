package com.fdddf.ocrinvoice.jmininvoice;

import com.netease.lowcode.core.annotation.NaslStructure;

@NaslStructure
public class InvoiceCheckRequest {
    /**
     * 发票号码
     */
    public String fphm;

    /**
     * 开票日期 格式YYYYMMDD
     */
    public String kprq;

    /**
     * 发票代码 【注意：全电票可不传，其他必传】
     */
    public String fpdm;

    /**
     * 校验码 【注意：专票、全电票可不传，其他必填。校验码支持全位和后6位】
     */
    public String xym;

    /**
     * 不含税金额 【注意：普票可不传，其他发票必填 。全电票请传含税金额，其他发票需传 不含税金额】
     */
    public String bhsje;

    public InvoiceCheckRequest() {
    }

    public InvoiceCheckRequest(String fphm, String kprq, String fpdm, String xym, String bhsje) {
        this.fphm = fphm;
        this.kprq = kprq;
        this.fpdm = fpdm;
        this.xym = xym;
        this.bhsje = bhsje;
    }

    public Boolean validate() {
        return fphm != null && kprq != null;
    }
}
