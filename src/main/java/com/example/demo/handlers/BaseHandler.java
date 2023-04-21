package com.example.demo.handlers;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.demo.configuration.AppConfiguration;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import com.example.demo.util.RestMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Map;

@Slf4j
public abstract class BaseHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final UserRepository userRepository;

    protected BaseHandler() {
        this.userRepository = AppConfiguration.getUserRepository();
    }

    protected BaseHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Exposed as method to allow mocking for tests.
     *
     * @return
     */
    protected UserRepository getRepository() {
        return userRepository;
    }


    protected ApiGatewayResponse getGoodUserResponse(User user) {
        return ApiGatewayResponse.builder()
                .setStatusCode(HttpStatus.SC_OK)
                .setObjectBody(RestMessageUtils.mapToDto(user))
                .setHeaders(RestMessageUtils.getCorsHeaders())
                .build();
    }

    protected ApiGatewayResponse getUserNotFoundResponse(String itemId) {
        return ApiGatewayResponse.builder()
                .setStatusCode(HttpStatus.SC_NOT_FOUND)
                .setObjectBody(RestMessageUtils.errorMessageAsMap("User for id: '" + itemId + "' not found."))
                .setHeaders(RestMessageUtils.getCorsHeaders())
                .build();
    }

    protected ApiGatewayResponse getInternalErrorResponse(Exception ex) {
        return ApiGatewayResponse.builder()
                .setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .setObjectBody(RestMessageUtils.errorMessageAsMap(ex.getMessage()))
                .setHeaders(RestMessageUtils.getCorsHeaders())
                .build();
    }
}
