#!/bin/sh
mvn clean package && \
rm -rf openshift/docker-deploy/quarkus-app && \
cp -r target/quarkus-app openshift/docker-deploy/quarkus-app && \
oc start-build camel-amq --from-dir=openshift/docker-deploy/ -F