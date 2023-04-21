package com.example.demo.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import com.example.demo.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
public class GetUserHandler extends BaseHandler {

    public GetUserHandler() {
        super();
    }

    public GetUserHandler(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        log.info("GET item request received ...");
        try {
            // get the 'pathParameters' from input
            Map<String, String> pathParameters = (Map<String, String>) input.get(Constants.PATH_PARAMETERS_LABEL);
            String itemId = pathParameters.get(User.ID_NAME);
            log.info("GET item request received for ID {}", itemId);

            // get the Product by id
            return ofNullable(getRepository().get(itemId))
                    .map(this::getGoodUserResponse)
                    .orElseGet(() -> getUserNotFoundResponse(itemId));

        } catch (Exception ex) {
            log.error("Error in retrieving item: ", ex);
            return getInternalErrorResponse(ex);
        }
    }

}
