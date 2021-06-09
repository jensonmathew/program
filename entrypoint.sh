#!/bin/bash

## Install Java
#apt-get update -y
#apt-get install openjdk-8-jdk -y 

## Run helloworld application
java -jar /home/*.jar

## Start a loop for prevent container interruption.
while true; do sleep 1000; done
