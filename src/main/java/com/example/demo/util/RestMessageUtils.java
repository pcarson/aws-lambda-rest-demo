package com.example.demo.util;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RestMessageUtils {

    private RestMessageUtils() {
    }

    /**
     * Utility to put a nessage in a map just so it can be returned as a Json error message.
     *
     * @param message
     * @return
     */
    public static Map<String, String> errorMessageAsMap(String message) {
        Map<String, String> mapMessage = new HashMap<>();
        mapMessage.put(Constants.ERROR_LABEL, message);
        return mapMessage;

    }

    public static UserDTO mapToDto(User user) {

        var dto = new UserDTO();
        dto.setId(user.getId());
        if (user.getCreated() != null) {
            dto.setCreated(user.getCreated().toString());
        }
        if (user.getLastModified() != null) {
            dto.setLastModified(user.getLastModified().toString());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            dto.setEmail(user.getEmail());
        }
        if (!StringUtils.isEmpty(user.getPassword())) {
            dto.setPassword(user.getPassword());
        }

        return dto;
    }

    public static List<UserDTO> mapToDto(List<User> pd) {

        return pd.stream().map(RestMessageUtils::mapToDto).collect(Collectors.toList());

    }

    public static Map<String, String> getCorsHeaders() {
        Map<String, String> headers = new HashMap<>();
        // Add default CORS Headers
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        return headers;
    }

}
