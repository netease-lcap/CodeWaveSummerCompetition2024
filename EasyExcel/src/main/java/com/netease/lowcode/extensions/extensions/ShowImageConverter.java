package com.netease.lowcode.extensions.extensions;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * LocalDate类型转换器
 */
@Component
public class ShowImageConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(!value.contains("codewave_excel_pic:")){
            return new WriteCellData<String>(value);
        }
        value = value.replace("codewave_excel_pic:", "");
        try {
            URI uri = new URI(value);
            URL imageUrl = new URL(uri.toASCIIString());
            InputStream imageStream = imageUrl.openStream();
            byte[] bytes = IOUtils.toByteArray(imageStream);
            return new WriteCellData<byte[]>(bytes);
        } catch (Exception e) {
            return new WriteCellData<String>(value);
        }

    }
}
