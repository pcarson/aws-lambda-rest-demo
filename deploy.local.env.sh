#!/bin/bash
source common.env.sh

export REGION=eu-central-1
export STAGE_NAME=local

exec "$@"
