#!/bin/bash

echo ""
echo "$(tput setaf 2)Creating 'solr' user $(tput sgr 0)"
echo ""

adduser solr
echo solr | passwd solr --stdin


echo ""
echo "$(tput setaf 2)Creating Solr install directory $(tput sgr 0)"
echo ""

mkdir /opt/solr
chown solr:solr /opt/solr

echo ""
echo "$(tput setaf 2)Creating HDFS directory for Solr data $(tput sgr 0)"
echo ""

su hdfs <<ENDCOMMANDS
hadoop fs -mkdir -p /user/solr
hadoop fs -chown solr /user/solr
ENDCOMMANDS

su solr <<ENDCOMMANDS
cd /opt/solr

echo ""
echo "$(tput setaf 2)Downloading and installing Solr $(tput sgr 0)"
echo ""

wget http://apache.mirrors.hoobly.com/lucene/solr/4.10.1/solr-4.10.1.tgz
tar xzf solr-4.10.1.tgz
ln -s solr-4.10.1 latest
rm -rf solr-*.tgz

echo ""
echo "$(tput setaf 2)Cleaning up Solr installation $(tput sgr 0)"
echo ""

# copy example directory to new directory called hdp
cp -r /opt/solr/latest/example /opt/solr/latest/hdp

# cleanup hdp directory
rm -rf /opt/solr/latest/hdp/example* /opt/solr/latest/hdp/multicore /opt/solr/latest/hdp/solr

# copy schemaless example
cp -r /opt/solr/latest/example/example-schemaless/solr /opt/solr/latest/hdp/solr
mv /opt/solr/latest/hdp/solr/collection1 /opt/solr/latest/hdp/solr/tweets
cp -r /opt/solr/latest/example/solr/collection1/conf/admin-*.html /opt/solr/latest/hdp/solr/tweets/conf
rm -rf /opt/solr/latest/hdp/solr/tweets/core.properties
ENDCOMMANDS

cp solrconfig.xml /opt/solr/latest/hdp/solr/tweets/conf

echo ""
echo "$(tput setaf 2)Finished installing Solr $(tput sgr 0)"
echo ""
