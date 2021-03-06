#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#echo $DIR

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 3 - Install Solr $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

echo ""
echo "$(tput setaf 2)##### Creating 'solr' user $(tput sgr 0)"
echo ""

adduser solr
echo solr | passwd solr --stdin


echo ""
echo "$(tput setaf 2)##### Creating Solr install directory $(tput sgr 0)"
echo ""

mkdir /opt/solr
chown solr:solr /opt/solr

echo ""
echo "$(tput setaf 2)##### Creating HDFS directory for Solr data $(tput sgr 0)"
echo ""

su hdfs <<ENDCOMMANDS
hadoop fs -mkdir -p /user/solr
hadoop fs -chown solr /user/solr
ENDCOMMANDS

su solr <<ENDCOMMANDS
cd /opt/solr

echo ""
echo "$(tput setaf 2)##### Downloading and installing Solr $(tput sgr 0)"
echo ""

wget -nv http://www.interior-dsgn.com/apache/lucene/solr/4.10.2/solr-4.10.2.tgz
tar xzf solr-4.10.2.tgz
ln -s solr-4.10.2 latest
rm -rf solr-*.tgz

echo ""
echo "$(tput setaf 2)##### Cleaning up Solr installation $(tput sgr 0)"
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

cp $DIR/solrconfig.xml /opt/solr/latest/hdp/solr/tweets/conf

echo ""
echo "$(tput setaf 2)##### Remove conflicting jars $(tput sgr 0)"
echo ""
rm -rf /usr/lib/storm/lib/httpclient-4.1.1.jar
rm -rf /usr/lib/storm/lib/httpcore-4.1.jar

echo ""
echo "$(tput setaf 2)##### Finished installing Solr $(tput sgr 0)"
echo ""
