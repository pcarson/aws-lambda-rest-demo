package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
/**
 * Convenience Object to allow variables to be set in tests
 * as well as used in code. (NB environment variables can't be set for tests).
 */
public class EnvironmentVariableHelper {

    private String dynamoHost;

    public static final String LOCALSTACK_DYNAMO_HOST = "LOCALSTACK_HOSTNAME";

    public EnvironmentVariableHelper() {
        dynamoHost = System.getenv(LOCALSTACK_DYNAMO_HOST);
        if (StringUtils.isEmpty(dynamoHost)) {
            // then fall back to a direct link to localstack dynamo ..
            dynamoHost = "127.0.0.1";
        }
    }

    /**
     * 'inside' localstack, our lambda code has to access
     * the dynamo service using the environment variable
     * LOCALSTACK_HOSTNAME. Hopefully it's set ...
     *
     * @return dynamo host
     */
    public String getDynamoHost() {
        dynamoHost = System.getenv(LOCALSTACK_DYNAMO_HOST);
        if (StringUtils.isEmpty(dynamoHost)) {
            // then fall back to a direct link to localstack dynamo ..
            dynamoHost = "127.0.0.1";
        }
        return dynamoHost;
    }
}
