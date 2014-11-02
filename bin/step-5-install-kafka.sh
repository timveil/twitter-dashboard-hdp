#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 5 - Install Kafka $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

mkdir /opt/kafka
cd /opt/kafka

echo ""
echo "$(tput setaf 2)##### Downloading Kafka $(tput sgr 0)"
echo ""


wget -nv http://apache.arvixe.com/kafka/0.8.1.1/kafka_2.9.2-0.8.1.1.tgz
echo ""
echo "$(tput setaf 2)##### Installing Kafka $(tput sgr 0)"
echo ""

tar xzf kafka_2.9.2-0.8.1.1.tgz
ln -s kafka_2.9.2-0.8.1.1 latest
rm -rf kafka_-*.tgz

echo ""
echo "$(tput setaf 2)##### Finished installing Kafka $(tput sgr 0)"
echo ""