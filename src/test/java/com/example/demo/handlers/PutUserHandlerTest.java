package com.example.demo.handlers;

import com.example.demo.BaseJsonTestUtils;
import com.example.demo.TestDataPreparationUtils;
import com.example.demo.entity.User;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PutUserHandlerTest extends BaseJsonTestUtils implements TestDataPreparationUtils {

    @InjectMocks
    private PutUserHandler handler;
    @Mock
    private UserRepository repository;

    @Test
    void updateOneUserHappyDays() {

        Map<String, Object> input = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "1");
        String bodyParms = String.format("{\"id\":\"%s\", \"email\":\"y@x.com\", \"password\":\"sssshhhhh\"}", "1");
        input.put("pathParameters", pathParams);
        input.put("body", bodyParms);
        when(repository.get(any())).thenReturn(new User());
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    void updateOneUserWithDetailNotFound() {

        Map<String, Object> input = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "meh");
        String bodyParms = String.format("{\"id\":\"%s\", \"email\":\"y@x.com\", \"password\":\"sssshhhhh\"}", "1");
        input.put("pathParameters", pathParams);
        input.put("body", bodyParms);
        when(repository.get(any())).thenReturn(null);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateOneUserThrowingHttp500ByPassingIncorrectParameterType() {

        Map<String, Object> input = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        input.put("body", parms);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}