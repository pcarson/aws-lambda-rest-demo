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

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllUsersHandlerTest extends BaseJsonTestUtils implements TestDataPreparationUtils {

    @InjectMocks
    private GetAllUsersHandler handler;
    @Mock
    private UserRepository repository;

    @Test
    void getAllUsersNotFound() {

        when(repository.getAll()).thenReturn(Collections.emptyList());
        ApiGatewayResponse response = handler.handleRequest(Map.of(), null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test
    void getAllUsersFound() {

        when(repository.getAll()).thenReturn(Arrays.asList(new User()));
        ApiGatewayResponse response = handler.handleRequest(Map.of(), null);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }
}