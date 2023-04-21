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
class DeleteUserHandlerTest extends BaseJsonTestUtils implements TestDataPreparationUtils {

    @InjectMocks
    private DeleteUserHandler handler;
    @Mock
    private UserRepository repository;

    @Test
    void deleteOneUserHappyDays() {

        var input = prepareInput("1");
        when(repository.get(any())).thenReturn(new User());
        var response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    void deleteOneUserWithDetailNotFound() {

        var input = prepareInput("meh");
        when(repository.get(any())).thenReturn(null);
        var response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateOneUserThrowingHttp500ByPassingIncorrectParameterType() {

        Map<String, Object> input = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        input.put("body", parms);
        var response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Map<String, Object> prepareInput(String id) {
        Map<String, Object> input = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", id);
        var bodyParms = String.format("{\"id\":\"1\"\"}");
        input.put("pathParameters", pathParams);
        input.put("body", bodyParms);
        return input;
    }

}