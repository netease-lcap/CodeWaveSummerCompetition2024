package com.netease.lowcode.lib.officetopdf.dto;

import com.netease.lowcode.core.EnvironmentType;
import com.netease.lowcode.core.annotation.Environment;
import com.netease.lowcode.core.annotation.NaslConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileTransferConfig {
    /**
     * 文件转换类型,1-使用apache；2-使用aspose
     */
    @Value("${transferType}")
    @NaslConfiguration(defaultValue = {@Environment(type = EnvironmentType.DEV, value = "1"),
            @Environment(type = EnvironmentType.ONLINE, value = "1")})
    public String transferType;
    /**
     * aspose证书地址，当transferType=2时必填
     */
    @Value("${licenseUrl}")
    @NaslConfiguration
    public String licenseUrl;

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
