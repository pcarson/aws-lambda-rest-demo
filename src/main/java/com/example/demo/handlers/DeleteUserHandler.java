package com.example.demo.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import com.example.demo.util.Constants;
import com.example.demo.util.RestMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Slf4j
public class DeleteUserHandler extends BaseHandler {

    public DeleteUserHandler() {
        super();
    }

    public DeleteUserHandler(UserRepository userRepository) {
        super(userRepository);
    }

    private Function<User, ApiGatewayResponse> convertToResponse = this::getGoodResponseNoBody;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        log.info("DELETE request received ...");
        try {
            // get the 'pathParameters' from input
            Map<String, String> pathParameters = (Map<String, String>) input.get(Constants.PATH_PARAMETERS_LABEL);
            var itemId = pathParameters.get(User.ID_NAME);
            log.info("DELETE item request received for ID {}", itemId);

            // send the response back
            return ofNullable(getRepository().get(itemId))
                    .map(this::deleteUser)
                    .map(convertToResponse::apply)
                    .orElseGet(() -> getUserNotFoundResponse(itemId));

        } catch (Exception ex) {
            log.error("Error in deleting item: ", ex);
            return getInternalErrorResponse(ex);
        }
    }

    private User deleteUser(User user) {
        log.info("Deleting {}", user.getId());
        getRepository().delete(user);
        return user;
    }

    private ApiGatewayResponse getGoodResponseNoBody(User user) {
        return ApiGatewayResponse.builder()
                .setStatusCode(HttpStatus.SC_OK)
                // NB localstack seems to have a problem with returning responses with no body
                // these become 502 Internal Service Errors - so send an empty string
                .setObjectBody("")
                .setHeaders(RestMessageUtils.getCorsHeaders())
                .build();
    }

}
