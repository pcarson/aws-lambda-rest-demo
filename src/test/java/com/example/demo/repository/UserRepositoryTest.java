package com.example.demo.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    DynamoDBMapper mapper;

    @InjectMocks
    UserRepository userRepository;

    @Mock
    PaginatedQueryList<User> queryResult;

    @Test
    void testCreateHappyDays() {
        var pd = new User();
        userRepository.save(pd);
        assertNotNull(pd.getId());
    }

    @Test
    void testGetReturnsAnItemHappyDays() {

        PaginatedQueryList<User> expectedResult = mock(PaginatedQueryList.class);
        when(mapper.query(eq(User.class), any(DynamoDBQueryExpression.class))).thenReturn(expectedResult);
        // when(expectedResult.isEmpty()).thenReturn(false);
        when(expectedResult.get(0)).thenReturn(new User());
        assertNotNull(userRepository.get(UUID.randomUUID().toString()));
    }

    @Test
    void testGetReturnsNothing() {

        PaginatedQueryList<User> expectedResult = mock(PaginatedQueryList.class);
        when(mapper.query(eq(User.class), any(DynamoDBQueryExpression.class))).thenReturn(expectedResult);
        // when(expectedResult.isEmpty()).thenReturn(true);
        assertNull(userRepository.get(UUID.randomUUID().toString()));
    }

}