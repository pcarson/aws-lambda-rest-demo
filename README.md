# aws-lambda-rest-demo

1. [Overview](#overview)
   1. [Serverless](#serverless)
   2. [Localstack](#localstack)
2. [Deployment](#deployment)
   1. [Development environment](#environment) 

### Overview
This (serverless) service exposes a REST API which adds items via a POST to the local-user table on localstack, and allows details of an item in the table to be exposed via a GET request.

As a SERVERLESS LAMBDA process, the code is executed only while required. 

#### Serverless
It uses the open source [serverless](https://serverless.com/framework/docs/) project, and the pre-requisite setup steps can be found [here](https://serverless.com/framework/docs/providers/aws/guide/quick-start/).

See [this](https://serverless.com/blog/how-to-create-a-rest-api-in-java-using-dynamodb-and-serverless/) blog post on Java and servless as a background info on the process.

For more information on the requirements to run this demo, se [here](https://docs.localstack.cloud/user-guide/integrations/serverless-framework/)

#### Localstack

This project is particularly configured to use the localstack AWS emulation platform, and a docker-compose.yml is included in the project to start this Docker container.

The customisations made to make this project runnable on localstack, and would therefore have to be reversed to deploy this lambda on the AWS network, are as follows:

* serverless.yml includes a dynamo definition under the resources tag, but it is commented out as it is currently ignored by the serverless-localstack plugin.
* to compensate for the commented out serverless definition, the dynamo table is created in code on first execution.
* AWS_REGION is specifically ignored (nullified) when configuring dynamo on localstack.
* for communication between the lambda and dynamo, i.e. both inside the localstack container, we have to specify the internal URL contained in the environment variable LOCALSTACK_HOSTNAME. 

### Deployment and run/test of this demo <a name="deployment"></a>
#### development environment: <a name="environment"></a>
This code was developed and tested on:
```agsl
* Linux 5.15.0-71-generic #78-Ubuntu x86_64 GNU/Linux
* OpenJDK Runtime Environment (build 17.0.6+10-Ubuntu-0ubuntu122.04)
$ serverless --version
Framework Core: 3.30.1
Plugin: 6.2.3
SDK: 4.3.2
```
#### pre-requisites: <a name="prerequisites"></a>
1) serverless installed locally
```agsl
npm install -g serverless
``` 
2) install the serverless-localstack plugin, e.g.:
```agsl
npm install -D serverless-localstack
```
3) install the serverless-deployment-bucket plugin, e.g.:
```agsl
npm install serverless-deployment-bucket --save-dev
```
#### - see the script to set these components up on linux
```agsl
./pre_requisite_installs.sh
```
4) install localstack locally - the docker-compose.yml which is part of the project can be used to start a copy of localstack.
   Installation requirements for localstack via docker-compose
* [docker](https://docs.docker.com/get-docker/)
* [docker-compose](https://docs.docker.com/compose/install/)

#### Install

1) start the localstack container in a separate session (as it will run interactively)
```
docker-compose build
docker-compose up
```
2) deploy the lambda
```agsl
./deploy.sh
```

### Manual Deployment
Make sure a localstack is running locally, e.g.:
```agsl
docker-compose up
```

Compile the Java components of this project - requires 
* a local Java SDK installation of at least Java 11
* a local install of maven if not using the maven wrapper mvnw as specified below

then:

```agsl
./mvnw clean org.jacoco:jacoco-maven-plugin:prepare-agent install -DskipITs=true
./mvnw clean install
```

then, run the following command to install the lambda on our local running localstack
```
rm -rf .serverless/ && source deploy.local.env.sh && serverless deploy --verbose --stage local
``` 

When the deployment to localstack has succeeded, we need to use items from the log output in order to configure access to our lambda, specifically
* api_keys - we have to use this value as the x-api-key value in any requests to our lambda
* endpoint - tells us where to access our API endpoints on localstack

```agsl
✔ Service deployed to stack local-com-example-demo-user (88s)

api keys:
  local-com-example-demo-user-api-key: 43L5bGN2ZmPxMsjKVaX6nvQDrohflAe8IRywkdOY
endpoint: http://localhost:4566/restapis/92zv24g2yc/local/_user_request_
functions:
  getUser: local-com-example-demo-user-local-getUser (18 MB)
  getUsers: local-com-example-demo-user-local-getUsers (18 MB)
  putUser: local-com-example-demo-user-local-putUser (18 MB)
  createUser: local-com-example-demo-user-local-createUser (18 MB)
  deleteUser: local-com-example-demo-user-local-deleteUser (18 MB)

Stack Outputs:
  ServerlessDeploymentBucketName: local-com-example-demo-serverless-bucket
  ServiceEndpoint: https://92zv24g2yc.execute-api.localhost.localstack.cloud:4566/local
```
### Testing the localstack deployment of the REST API

Using the api key and endpoint from above (these will change with every new deployment/new startup of localstack) we can format an HTTP request, as per the following examples.

Create a new User item on localstack:
```agsl
curl --request POST \
  --url http://localhost:4566/restapis/92zv24g2yc/local/_user_request_/users \
  --header 'Content-Type: application/json' \
  --header 'x-api-key: 43L5bGN2ZmPxMsjKVaX6nvQDrohflAe8IRywkdOY' \
  --cookie JSESSIONID=DA09B324246A3CFB37F57C7D8506BA1A \
  --data '{
	"email": "testing2@test.com",
	"password":"ssshhh"
}'
```

Retrieve ALL users from the dynamoDB within localstack:
```agsl
curl --request GET \
  --url http://localhost:4566/restapis/92zv24g2yc/local/_user_request_/users \
  --header 'x-api-key: 43L5bGN2ZmPxMsjKVaX6nvQDrohflAe8IRywkdOY' \
  --cookie JSESSIONID=DA09B324246A3CFB37F57C7D8506BA1A
```

Retrieve ONE user from the dynamoDB within localstack by specifying its ID value:
```agsl
curl --request GET \
  --url http://localhost:4566/restapis/92zv24g2yc/local/_user_request_/user/4a808eff-4144-44b9-8124-6d78761d53ff \
  --header 'x-api-key: 43L5bGN2ZmPxMsjKVaX6nvQDrohflAe8IRywkdOY' \
  --cookie JSESSIONID=DA09B324246A3CFB37F57C7D8506BA1A
```
Delete ONE user from the dynamoDB within localstack by specifying its ID value:
```agsl
curl --request DELETE \
  --url http://localhost:4566/restapis/92zv24g2yc/local/_user_request_/user/546527e4-69ef-4d60-8093-4ca4f65476ef \
  --header 'ContentType: application/json' \
  --header 'x-api-key: 43L5bGN2ZmPxMsjKVaX6nvQDrohflAe8IRywkdOY' \
  --cookie JSESSIONID=DA09B324246A3CFB37F57C7D8506BA1A
```
#### Viewing dynamo data added via the REST API
If you are running localstack via the included docker-compose file, it also starts a dynamo viewer container. Access this via
```agsl
http://localhost:8001/
```
where the dynamo table contents can be viewed.

### Testing the Java code

#### dynamo integration tests
Due to the maven dynamo testing libraries (see pom.xml) we can start a local copy of dynamoDB and run a dynamo integration test.

#### Java unit tests 
Can be written as per any other Java application
Jacoco has been configured as part of the maven build, and when the project is built locally, e.g. with
```agsl
./mvnw clean install
```
then the code coverage data can be found at aws-lambda-rest-demo/target/site/jacoco/index.html

TODO - expand this README ........
