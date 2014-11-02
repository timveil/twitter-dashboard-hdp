#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 5 - Install Kafka $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

echo ""
echo "$(tput setaf 2)##### Downloading Kafka $(tput sgr 0)"
echo ""

wget -nv http://public-repo-1.hortonworks.com/HDP-LABS/Projects/kafka/0.8.1/centos6/kafka-0.8.1.2.1.4.0-632.el6.noarch.rpm

echo ""
echo "$(tput setaf 2)##### Installing Kafka RPM $(tput sgr 0)"
echo ""

rpm -ivh kafka-0.8.1.2.1.4.0-632.el6.noarch.rpm
rm -rf kafka-0.8.1.2.1.4.0-632.el6.noarch.rpm

echo ""
echo "$(tput setaf 2)##### Finished installing Kafka $(tput sgr 0)"
echo ""