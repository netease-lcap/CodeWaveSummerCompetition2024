package com.netease.lowcode.custonapifilter.sign;

public interface SignatureService {

    Boolean signature(String data, String key, String sign);

    String type();
}
