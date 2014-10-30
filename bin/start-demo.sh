#!/bin/bash

echo ""
echo "$(tput setaf 2)Starting Solr $(tput sgr 0)"
echo ""

nohup java -jar /opt/solr/latest/hdp/start.jar -Djetty.home=/opt/solr/latest/hdp -Dsolr.solr.home=/opt/solr/latest/hdp/solr &> solr.out&

echo ""
echo "$(tput setaf 2)Creating 'tweets' core $(tput sgr 0)"
echo ""

curl "http://sandbox.hortonworks.com:8983/solr/admin/cores?action=CREATE&name=tweets&instanceDir=/opt/solr/latest/hdp/solr/tweets"

echo ""
echo "$(tput setaf 2)Starting Kafka $(tput sgr 0)"
echo ""

service kafka start

echo ""
echo "$(tput setaf 2)Attempting to create Kafka topic $(tput sgr 0)"
echo ""

/usr/lib/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic tweets

echo ""
echo "$(tput setaf 2)Deploying Storm topology $(tput sgr 0)"
echo ""
#storm jar storm-1.0-SNAPSHOT.jar dashboard.storm.Harness
