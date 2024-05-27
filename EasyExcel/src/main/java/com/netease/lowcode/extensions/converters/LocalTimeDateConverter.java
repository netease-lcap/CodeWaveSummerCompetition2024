package com.netease.lowcode.extensions.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.WorkBookUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalTimeDateConverter implements Converter<LocalTime> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalTime.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        LocalDateTime localDateTime = value == null ? null : value.atDate(LocalDate.now());
        WriteCellData<?> cellData = new WriteCellData<>(localDateTime);
        String format = null;
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            format = contentProperty.getDateTimeFormatProperty().getFormat();
        }
        WorkBookUtil.fillDataFormat(cellData, format, "HH:mm:ss");
        return cellData;
    }

    @Override
    public LocalTime convertToJavaData(ReadConverterContext<?> context) throws Exception {

        if(context.getReadCellData().getType().equals(CellDataTypeEnum.NUMBER)) {
            LocalDateTime localDateTime = DateUtils.getLocalDateTime(context.getReadCellData().getNumberValue().doubleValue(), false);
            return localDateTime.toLocalTime();
        }

        return Converter.super.convertToJavaData(context);
    }
}

