package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface TestDataPreparationUtils {

    default Map<String, Object> setupRequestParameters(String requestedId) {
        Map<String, Object> input = new HashMap<>();
        Map<String, String> parms = new HashMap<>();
        parms.put("id", requestedId);
        input.put("pathParameters", parms);
        return input;
    }

    default String createNewUser() {

        var repo = new UserRepository();
        var pd = new User();
        repo.save(pd);
        assertNotNull(pd.getId());
        return pd.getId();
    }
}