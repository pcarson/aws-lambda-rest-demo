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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserHandlerTest extends BaseJsonTestUtils implements TestDataPreparationUtils {

    @InjectMocks
    private GetUserHandler handler;
    @Mock
    private UserRepository repository;

    @Test
    void getOneUserNotFound() {

        Map<String, Object> input = setupRequestParameters("-1");
        when(repository.get(any())).thenReturn(null);
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getOneUserFound() {

        Map<String, Object> input = setupRequestParameters("-1");
        when(repository.get(any())).thenReturn(new User());
        ApiGatewayResponse response = handler.handleRequest(input, null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }
}