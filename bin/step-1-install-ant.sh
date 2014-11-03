#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#echo $DIR

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 1 - Install Ant $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

mkdir /opt/ant
cd /opt/ant

echo ""
echo "$(tput setaf 2)##### Downloading Ant $(tput sgr 0)"
echo ""
wget -nv http://mirror.symnds.com/software/Apache//ant/binaries/apache-ant-1.9.4-bin.tar.gz


echo ""
echo "$(tput setaf 2)##### Installing Ant $(tput sgr 0)"
echo ""

tar xzf apache-ant-1.9.4-bin.tar.gz
rm -rf apache-ant-1.9.4-bin.tar.gz
ln -s /opt/ant/apache-ant-1.9.4 /opt/ant/latest
echo 'ANT_HOME=/opt/ant/latest' >> ~/.bashrc
echo 'PATH=$PATH:$ANT_HOME/bin' >> ~/.bashrc

echo ""
echo "$(tput setaf 2)##### Finished installing Ant $(tput sgr 0)"
echo ""