#! /usr/bin/env bash

if [ -z "$STAGE_NAME" ]; then
    export STAGE_NAME=local
fi

rm -rf .serverless/ && source deploy.local.env.sh && serverless deploy --verbose --stage local