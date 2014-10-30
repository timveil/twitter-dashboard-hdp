#!/bin/bash

cd /root

# create solr user
adduser solr
echo solr | passwd solr --stdin

# create solr home directory
mkdir /opt/solr
chown solr:solr /opt/solr

# create necessary hdfs directory
su hdfs <<ENDCOMMANDS
hadoop fs -mkdir -p /user/solr
hadoop fs -chown solr /user/solr
ENDCOMMANDS

su solr <<ENDCOMMANDS
cd /opt/solr

# download and install solr
wget http://apache.mirrors.hoobly.com/lucene/solr/4.10.1/solr-4.10.1.zip
unzip -q solr-4.10.1.zip
rm -rf solr-*.zip

# download and install banana
wget https://github.com/LucidWorks/banana/archive/banana-1.4.tar.gz
tar -zxf banana-1.4.tar.gz
rm -rf banana-*.gz

# copy example directory to new directory called hdp
cp -r /opt/solr/solr-4.10.1/example /opt/solr/solr-4.10.1/hdp

# cleanup hdp directory
rm -rf /opt/solr/solr-4.10.1/hdp/example* /opt/solr/solr-4.10.1/hdp/multicore /opt/solr/solr-4.10.1/hdp/solr

# copy schemaless example
cp -r /opt/solr/solr-4.10.1/example/example-schemaless/solr /opt/solr/solr-4.10.1/hdp/solr
mv /opt/solr/solr-4.10.1/hdp/solr/collection1 /opt/solr/solr-4.10.1/hdp/solr/tweets
cp -r /opt/solr/solr-4.10.1/example/solr/collection1/conf/admin-*.html /opt/solr/solr-4.10.1/hdp/solr/tweets/conf
rm -rf /opt/solr/solr-4.10.1/hdp/solr/tweets/core.properties
sed -i 's/..\/..\/..\/..\//..\/..\/..\//g' /opt/solr/solr-4.10.1/hdp/solr/tweets/conf/solrconfig.xml

# build banana
sed -i 's/localhost/sandbox.hortonworks.com/g' /opt/solr/banana-banana-1.4/src/config.js
sed -i 's/logstash_logs/tweets/g' /opt/solr/banana-banana-1.4/src/config.js

cd /opt/solr/banana-banana-1.4/src/app/dashboards
ls | grep -v 'default-ts.json' | xargs rm
mv default-ts.json default.json
sed -i 's/New Time Series Dashboard/Twitter Dashboard/g' /opt/solr/banana-banana-1.4/src/app/dashboards/default.json
sed -i 's/event_timestamp/createdAt/g' /opt/solr/banana-banana-1.4/src/app/dashboards/default.json
sed -i 's/localhost/sandbox.hortonworks.com/g' /opt/solr/banana-banana-1.4/src/app/dashboards/default.json
sed -i 's/collection1/tweets/g' /opt/solr/banana-banana-1.4/src/app/dashboards/default.json
sed -i 's/message/screenName/g' /opt/solr/banana-banana-1.4/src/app/dashboards/default.json

cd /opt/solr/banana-banana-1.4
mkdir build
yum -y install ant
ant

# copy banana war to solr
cp /opt/solr/banana-banana-1.4/build/banana-0.war /opt/solr/solr-4.10.1/hdp/webapps/banana.war
cp /opt/solr/banana-banana-1.4/jetty-contexts/banana-context.xml /opt/solr/solr-4.10.1/hdp/contexts/
ENDCOMMANDS
