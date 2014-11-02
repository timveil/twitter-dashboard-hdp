#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo DIR

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 4 - Install Banana $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

echo ""
echo "$(tput setaf 2)##### Creating banana install directory $(tput sgr 0)"
echo ""

mkdir /opt/banana
chown solr:solr /opt/banana

cd /opt/banana

echo ""
echo "$(tput setaf 2)##### Downloading and installing Banana $(tput sgr 0)"
echo ""

wget -nv https://github.com/LucidWorks/banana/archive/banana-1.4.tar.gz
tar xzf banana-1.4.tar.gz
ln -s banana-banana-1.4 latest
rm -rf banana-*.gz

echo ""
echo "$(tput setaf 2)##### Cleaning up Banana source code $(tput sgr 0)"
echo ""

sed -i 's/localhost/sandbox.hortonworks.com/g' /opt/banana/latest/src/config.js
sed -i 's/logstash_logs/tweets/g' /opt/banana/latest/src/config.js

cd /opt/banana/latest/src/app/dashboards
rm -rf *.json
mv default-ts.json default.json
cp $DIR/default.json /opt/banana/latest/src/app/dashboards

echo ""
echo "$(tput setaf 2)##### Building Banana with Ant $(tput sgr 0)"
echo ""

cd /opt/banana/latest
mkdir build
ant

echo ""
echo "$(tput setaf 2)##### Copying Banana war to Solr $(tput sgr 0)"
echo ""

cp /opt/banana/latest/build/banana-0.war /opt/solr/latest/hdp/webapps/banana.war
cp /opt/banana/latest/jetty-contexts/banana-context.xml /opt/solr/latest/hdp/contexts/

echo ""
echo "$(tput setaf 2)##### Finished installing Banana $(tput sgr 0)"
echo ""