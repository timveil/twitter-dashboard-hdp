#!/bin/bash

rm -rf *.log *.out

rm -rf /opt/ant
rm -rf /opt/maven
rm -rf /opt/solr
rm -rf /opt/banana
rm -rf /opt/kafka

userdel -r solr
hadoop fs -rm -R -f /user/solr
hadoop fs -rm -R -f /tweets/staging
