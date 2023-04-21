package com.example.demo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
/**
 * Test basic String -> LocalDateTime convert/unconvert
 */
class DynamoDBLocalDateTimeConverterTest {

    final LocalDateTime localDateTime =
            LocalDateTime.of(2024, 12, 31, 8, 45, 20, 111);

    private DynamoDBLocalDateTimeConverter converter = new DynamoDBLocalDateTimeConverter();

    @Test
    void convert() {
        assertEquals("2024-12-31T08:45:20.000000111", converter.convert(localDateTime));
    }

    @Test
    void unconvert() {
        assertEquals(localDateTime, converter.unconvert("2024-12-31T08:45:20.000000111"));
    }

}