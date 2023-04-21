#!/usr/bin/env bash

echo Setting up serverless framework ...

npm install -g serverless
npm ci

npm install --save-dev serverless-localstack
npm install --save-dev serverless-deployment-bucket

echo Maven Build ...

mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -DskipITs=true
mvn clean install