package com.example.demo.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import com.example.demo.util.Constants;
import com.example.demo.util.RestMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
public class GetAllUsersHandler extends BaseHandler {

    public GetAllUsersHandler() {
        super();
    }

    public GetAllUsersHandler(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        log.info("GET ALL items request received ...");
        try {
            // get the Product by id
            List<User> results = getRepository().getAll();
            return getResponse(results);

        } catch (Exception ex) {
            log.error("Error in retrieving item: ", ex);
            return getInternalErrorResponse(ex);
        }
    }

    private ApiGatewayResponse getResponse(List<User> users) {
        return ApiGatewayResponse.builder()
                .setStatusCode(HttpStatus.SC_OK)
                .setObjectBody(RestMessageUtils.mapToDto(users))
                .setHeaders(RestMessageUtils.getCorsHeaders())
                .build();
    }

}
