package com.example.demo.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility to convert LocalDateTime to dynamo representation and back
 */
public class DynamoDBLocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER;

    public DynamoDBLocalDateTimeConverter() {
    }

    public String convert(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.format(DATE_TIME_FORMATTER);
    }

    public LocalDateTime unconvert(String input) {
        return LocalDateTime.parse(input, DATE_TIME_FORMATTER);
    }

    static {
        DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }
}
