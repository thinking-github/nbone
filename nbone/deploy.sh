#!/bin/bash
# clean package clean deploy
# mvn  -U -Dmaven.test.skip=true package deploy -Prelease  -X
mvn  -U -Dmaven.test.skip=true package deploy