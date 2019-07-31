#!/bin/sh
sudo mvn clean package -DskipTests dockerfile:build
sudo docker-compose up