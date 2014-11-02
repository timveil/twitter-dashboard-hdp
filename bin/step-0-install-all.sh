#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo DIR

yum clean all 2>&1 /dev/null
yum clean metadata 2>&1 /dev/null
yum clean dbcache 2>&1 /dev/null
yum makecache 2>&1 /dev/null

./step-1-install-ant.sh
./step-2-install-maven.sh

source ~/.bashrc

./step-3-install-solr.sh
./step-4-install-banana.sh
./step-5-install-kafka.sh
./step-6-install-nodejs.sh