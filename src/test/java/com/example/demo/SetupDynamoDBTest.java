package com.example.demo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.amazonaws.regions.Regions.EU_CENTRAL_1;

@Slf4j
public abstract class SetupDynamoDBTest {

    /**
     * We have to create the table here before each integration test.
     * Note that with serverless deployment, this is managed via serverless.yml definitions, so no
     * non-test table creation code is required - it is only for testing, along with pom definitions.
     */
    public void dynamoSetup() {

        final String dynamodbEndpointFromEnv =
                System.getProperty("dynamodb.endpoint", "http://localhost:8000");

        final String dynamodbEndpoint =
                dynamodbEndpointFromEnv.endsWith(":") ?
                        dynamodbEndpointFromEnv + "8000" :
                        dynamodbEndpointFromEnv;

        log.info("Using dynamoEndpoint {}, ", dynamodbEndpoint);

        final AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                dynamodbEndpoint, EU_CENTRAL_1.toString());

        var client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .build();

        DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(User.TABLE_NAME))
                .build();

        List<KeySchemaElement> keySchema = Collections
                .singletonList(new KeySchemaElement().withAttributeName(User.ID_NAME).withKeyType(KeyType.HASH));
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();

        attributeDefinitions
                .add(new AttributeDefinition().withAttributeName(User.ID_NAME)
                        .withAttributeType(ScalarAttributeType.S)
                );
        attributeDefinitions
                .add(new AttributeDefinition().withAttributeName(User.EMAIL_NAME)
                        .withAttributeType(ScalarAttributeType.S)
                );
        List<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<>();

        globalSecondaryIndexes
                .add(new GlobalSecondaryIndex().withIndexName(User.EMAIL_INDEX).withKeySchema(
                                new KeySchemaElement().withAttributeName(User.EMAIL_NAME).withKeyType(KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L)));

        var request = new CreateTableRequest().withTableName(User.TABLE_NAME)
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withGlobalSecondaryIndexes(globalSecondaryIndexes)
                .withProvisionedThroughput(
                        new ProvisionedThroughput()
                                .withReadCapacityUnits(1L)
                                .withWriteCapacityUnits(1L))
                .withStreamSpecification(
                        new StreamSpecification()
                                .withStreamEnabled(true)
                                .withStreamViewType(StreamViewType.NEW_AND_OLD_IMAGES));

        TableUtils.createTableIfNotExists(client, request);

    }

}