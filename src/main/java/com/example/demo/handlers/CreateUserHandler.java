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
import java.util.UUID;

@Slf4j
public class CreateUserHandler extends BaseHandler {

    public CreateUserHandler() {
        super();
    }

    public CreateUserHandler(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {

            // get the 'body' from input
            var body = ObjectMapperHolder.OBJECT_MAPPER.readTree((String) input.get(Constants.BODY_LABEL));

            // create the Process object for post
            var user = new User();

            // Always a new ID for creation ...
            user.setId(UUID.randomUUID().toString());

            var email = (body.get(User.EMAIL_NAME) == null ? "" : body.get(User.EMAIL_NAME).asText());
            user.setEmail(null);
            if (StringUtils.isNotEmpty(email)) {
                user.setEmail(email);
            }

            var password = (body.get(User.PASSWORD_NAME) == null ? "" : body.get(User.PASSWORD_NAME).asText());
            user.setPassword(null);
            if (StringUtils.isNotEmpty(password)) {
                user.setPassword(password);
            }

            getRepository().save(user);

            return getGoodUserResponse(user);

        } catch (Exception ex) {
            log.error("Error in saving item: ", ex);
            // send the error response back
            return getInternalErrorResponse(ex);
        }
    }

}
