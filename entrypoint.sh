#!/bin/sh

## Run helloworld application
java -jar /home/*.jar

## Start a loop for prevent container interruption.
while true; do sleep 1000; done
