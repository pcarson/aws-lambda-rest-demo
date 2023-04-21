package com.example.demo.configuration;

import com.example.demo.repository.UserRepository;
import com.example.demo.util.EnvironmentVariableHelper;

public class AppConfiguration {
    private static UserRepository userRepository;
    private static EnvironmentVariableHelper environmentVariableHelper;
    
    private AppConfiguration() {
    }

    public static UserRepository getUserRepository() {
        if (userRepository != null) {
            return userRepository;
        }

        userRepository = new UserRepository();
        return userRepository;
    }

    public static EnvironmentVariableHelper getEnvironmentVariableHelper() {
        if (environmentVariableHelper != null) {
            return environmentVariableHelper;
        }

        environmentVariableHelper = new EnvironmentVariableHelper();
        return environmentVariableHelper;
    }

}
