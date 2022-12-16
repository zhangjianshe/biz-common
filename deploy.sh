#!/usr/bin/env sh
mvn clean package install deploy -P%1
