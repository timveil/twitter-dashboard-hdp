#!/bin/bash

./step-1-install-ant.sh
./step-2-install-maven.sh

source ~/.bashrc

./step-3-install-solr.sh
./step-4-install-banana.sh
./step-5-install-kafka.sh
./step-6-install-nodejs.sh