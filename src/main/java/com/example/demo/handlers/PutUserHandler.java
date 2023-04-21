package com.example.demo.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiGatewayResponse;
import com.example.demo.util.Constants;
import com.example.demo.util.ObjectMapperHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public class PutUserHandler extends BaseHandler {

    private static final String METRICS_ACTION = "put";

    public PutUserHandler() {
        super();
    }

    public PutUserHandler(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            // get the 'pathParameters' from input
            Map<String, String> pathParameters = (Map<String, String>) input.get(Constants.PATH_PARAMETERS_LABEL);
            var itemId = pathParameters.get(User.ID_NAME);
            log.info("PUT item request received for ID {}", itemId);

            // get the Product by id
            var user = getRepository().get(itemId);
            if (user == null) {
                // Not Found
                // send the response back
                return getUserNotFoundResponse(itemId);
            }

            // Apply updates to User
            // get the 'body' from input
            var body = ObjectMapperHolder.OBJECT_MAPPER.readTree((String) input.get(Constants.BODY_LABEL));

            log.info("Body on request {}", (String) input.get(Constants.BODY_LABEL));

            var email = (body.get(User.EMAIL_NAME) == null ? "" : body.get(User.EMAIL_NAME).asText());
            if (StringUtils.isNotEmpty(email)) {
                user.setEmail(email);
            }

            var password = (body.get(User.PASSWORD_NAME) == null ? "" : body.get(User.PASSWORD_NAME).asText());
            if (StringUtils.isNotEmpty(password)) {
                user.setEmail(password);
            }

            getRepository().save(user);

            return getGoodUserResponse(user);

        } catch (Exception ex) {
            log.error("Error in updating item: ", ex);
            return getInternalErrorResponse(ex);
        }
    }
}
