package com.example.demo.repository;

import com.example.demo.SetupDynamoDBTest;
import com.example.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
/**
 * This test is excluded from the build pipeline tests as an *IntegrationTest exclusion.
 * To run locally, dynamoDB needs to be running as localstack on port 8080.
 */
class UserRepositoryIntegrationTest extends SetupDynamoDBTest {

    @BeforeEach
    /**
     * We have to create the table here before each integration test.
     * Note that with serverless deployment, this is managed via serverless.yml definitions, so no
     * non-test table creation code is required - it is only for testing, along with pom definitions.
     */
    void setup() {

        this.dynamoSetup();

    }

    @Test
    void testSaveAndGet() {

        var repo = new UserRepository();
        var pd = new User();
        repo.save(pd);
        assertNotNull(pd.getId());

        // now retrieve the same item ....

        var retrievedPd = repo.get(pd.getId());

        assertNotNull(retrievedPd);
        assertEquals(retrievedPd.getCreated(), pd.getCreated());
        assertEquals(retrievedPd.getLastModified(), pd.getLastModified());

        // save it again to update last modified
        repo.save(pd);
        assertEquals(retrievedPd.getCreated(), pd.getCreated());
        assertNotEquals(retrievedPd.getLastModified(), pd.getLastModified());

    }

    @Test
    void testBetByEmail() {

        var email = "findme@x.com";
        var repo = new UserRepository();
        var user = new User();
        user.setEmail(email);
        repo.save(user);
        assertNotNull(user.getId());

        // now retrieve the same item ....

        var userList = repo.findUserByEmailAddress(email);

        assertEquals(1, userList.size());
        assertEquals(email, userList.get(0).getEmail());
    }

}