package com.netease.lowcode.extensions.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.WorkBookUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeDateConverter implements Converter<ZonedDateTime> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return ZonedDateTime.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(ZonedDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {

        LocalDateTime localDateTime = value == null ? null : value.toLocalDateTime();
        WriteCellData<?> cellData = new WriteCellData<>(localDateTime);
        String format = null;
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            format = contentProperty.getDateTimeFormatProperty().getFormat();
        }
        WorkBookUtil.fillDataFormat(cellData, format, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return cellData;
    }

    @Override
    public ZonedDateTime convertToJavaData(ReadConverterContext<?> context) throws Exception {

        if(context.getReadCellData().getType().equals(CellDataTypeEnum.NUMBER)){
            LocalDateTime localDateTime = DateUtils.getLocalDateTime(context.getReadCellData().getNumberValue().doubleValue(), false);
            ZoneId zoneId = ZoneId.of("UTC");
            return localDateTime.atZone(zoneId);
        }

        return Converter.super.convertToJavaData(context);
    }
}
