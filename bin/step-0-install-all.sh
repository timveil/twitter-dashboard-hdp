#!/bin/bash

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 0 - Installing everything $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""



DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#echo $DIR

echo ""
echo "$(tput setaf 2)##### Cleaning up YUM $(tput sgr 0)"
echo ""

yum clean all  > /dev/null 2>&1
yum clean metadata  > /dev/null 2>&1
yum clean dbcache  > /dev/null 2>&1
yum makecache  > /dev/null 2>&1

./step-1-install-ant.sh
./step-2-install-maven.sh

source ~/.bashrc

./step-3-install-solr.sh
./step-4-install-banana.sh
./step-5-install-kafka.sh
./step-6-install-nodejs.sh