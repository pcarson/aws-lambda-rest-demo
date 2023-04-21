package com.example.demo.configuration;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.example.demo.entity.User;
import com.example.demo.util.EnvironmentVariableHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
public class DynamoDBConfiguration {
    private static AmazonDynamoDB amazonDynamoDB;
    private static DynamoDBMapperConfig dynamoDBMapperConfig;
    private static DynamoDBMapper dynamoDBMapper;
    private static DynamoDBConfiguration instance;

    private final EnvironmentVariableHelper environmentVariableHelper;

    private DynamoDBConfiguration() {
        environmentVariableHelper = AppConfiguration.getEnvironmentVariableHelper();
    }

    public static DynamoDBConfiguration getInstance() {
        if (instance == null) {
            instance = new DynamoDBConfiguration();
        }
        return instance;
    }

    public AmazonDynamoDB amazonDynamoDB() {
        if (amazonDynamoDB != null) {
            return amazonDynamoDB;
        }

        final AmazonDynamoDBClientBuilder builder;

        /*
        NB for build and tests, we expect the property dynamodb.endpoint
        to have been set by the maven plugin. Otherwise, we're expecting localstack to be up
        to run locally, and as this lambda runs inside localstack (as does dynamo) we need to use
        the internal Dynamo endpoint.
         */
        final String dynamodbEndpointFromEnv =
                System.getProperty("dynamodb.endpoint", getInternalLocalstackDynamoEndpoint());

        final String dynamodbEndpoint =
                dynamodbEndpointFromEnv.endsWith(":") ?
                        dynamodbEndpointFromEnv + "4566" :
                        dynamodbEndpointFromEnv;

        final EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
                dynamodbEndpoint, null); // No region specified for localstack

        builder = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration);

        log.info("Creating dynamodb client, endpoint={}, instanceId={}",
                ofNullable(builder.getEndpoint())
                        .map(EndpointConfiguration::getServiceEndpoint)
                        .orElse("UNKNOWN"));
        amazonDynamoDB = builder.build();
        checkWhetherTableExists();
        return amazonDynamoDB;
    }

    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        if (dynamoDBMapperConfig != null) {
            return dynamoDBMapperConfig;
        }

        DynamoDBMapperConfig.Builder builder = new DynamoDBMapperConfig.Builder();
        builder.setTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(User.TABLE_NAME));

        dynamoDBMapperConfig = builder.build();
        return dynamoDBMapperConfig;
    }

    public DynamoDBMapper dynamoDBMapper() {
        if (dynamoDBMapper != null) {
            return dynamoDBMapper;
        }

        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
        return dynamoDBMapper;
    }

    private String getInternalLocalstackDynamoEndpoint() {
        return "http://" + environmentVariableHelper.getDynamoHost() + ":4566";
    }

    public void checkWhetherTableExists() {
        if (amazonDynamoDB.listTables().getTableNames().isEmpty()) {
            // then we need to create our table ...
            createDynamoTableIfNotExists();
        }
    }

    private void createDynamoTableIfNotExists() {
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

        TableUtils.createTableIfNotExists(amazonDynamoDB, request);
    }

}
