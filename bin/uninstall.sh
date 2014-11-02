#!/bin/bash

rm -rf *.log *.out

rm -rf /opt/ant
rm -rf /opt/maven
rm -rf /opt/solr
rm -rf /opt/banana

rpm -e kafka-0.8.1.2.1.4.0-632.el6.noarch

userdel -r solr
hadoop fs -rm -R -f /user/solr
hadoop fs -rm -R -f /tweets/staging
