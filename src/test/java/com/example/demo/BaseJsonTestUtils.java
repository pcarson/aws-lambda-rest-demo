package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public abstract class BaseJsonTestUtils {

    protected ObjectMapper objectMapper = mapperWithDefaultDateSerializer();

    protected String loadContent(String filename) throws IOException {
        return IOUtils
                .toString(getClass().getResourceAsStream(filename), "UTF-8");
    }

    private ObjectMapper mapperWithDefaultDateSerializer() {

        // NOTE that the Autowired ObjectMapper defaults this serializer
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;

    }

}