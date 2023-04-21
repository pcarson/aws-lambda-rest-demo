package com.example.demo.handlers;

import com.example.demo.BaseJsonTestUtils;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreateUserHandlerTest extends BaseJsonTestUtils {

    @InjectMocks
    private CreateUserHandler handler;
    @Mock
    private UserRepository repository;

    @Test
    void createOneUser() {

        Map<String, Object> input = new HashMap<>();
        String parms = new String("{\"id\":\"1\", \"email\":\"x@y.com\"}");
        input.put("body", parms);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    void createOneUserThrowingHttp500ByPassingIncorrectParameterType() {

        Map<String, Object> input = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        input.put("body", parms);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}