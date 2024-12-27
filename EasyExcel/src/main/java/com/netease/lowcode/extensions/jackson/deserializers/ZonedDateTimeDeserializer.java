package com.netease.lowcode.extensions.jackson.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ZoneId zoneId = ZoneId.of("UTC");

        String value = jsonParser.getText();
        //兼容时间戳类型的txt
        try {
            //1560924503.445000000
            Timestamp timestamp = Timestamp.from(Instant.ofEpochMilli((long) (Double.parseDouble(jsonParser.getText()) * 1000)));
            return ZonedDateTime.ofInstant(timestamp.toInstant(), zoneId);
        } catch (NumberFormatException e) {
            //2019-06-19T06:08:23.445Z
            return ZonedDateTime
                    .parse(value, DateTimeFormatter.ISO_DATE_TIME)
                    .withZoneSameInstant(zoneId);
        }
    }
}
