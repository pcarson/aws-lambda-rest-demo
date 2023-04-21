package com.example.demo.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.demo.configuration.DynamoDBConfiguration;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UserRepository {

    private final DynamoDBMapper mapper;

    public UserRepository() {
        mapper = DynamoDBConfiguration.getInstance().dynamoDBMapper();
    }

    public UserRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public void save(User user) {

        if (StringUtils.isEmpty(user.getId())) {
            user.setId(UUID.randomUUID().toString());
        }

        var now = LocalDateTime.now();
        if (user.getCreated() == null) {
            user.setCreated(now);
        }
        user.setLastModified(now);
        log.info("User - save(): {}", user.toString());
        this.mapper.save(user);

    }

    public User get(String id) {

        User user = null;
        HashMap<String, AttributeValue> av = new HashMap<>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<User> queryExp = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<User> result = this.mapper.query(User.class, queryExp);

        if (result.isEmpty()) {
            log.info("User - get(): item - Not Found.");
        } else {
            user = result.get(0);
            log.info("User - get(): item - {}", user.toString());
        }

        return user;
    }

    public void delete(User user) {
        this.mapper.delete(user);
    }

    public List<User> getAll() {
        return mapper.scan(User.class, new DynamoDBScanExpression());
    }

}
