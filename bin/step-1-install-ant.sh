#!/bin/bash

mkdir /opt/ant
cd /opt/ant

echo ""
echo "$(tput setaf 2)Downloading Ant $(tput sgr 0)"
wget http://mirror.symnds.com/software/Apache//ant/binaries/apache-ant-1.9.4-bin.tar.gz
echo ""

echo ""
echo "$(tput setaf 2)Installing Ant $(tput sgr 0)"
echo ""

tar xzf apache-ant-1.9.4-bin.tar.gz
rm -rf apache-ant-1.9.4-bin.tar.gz
ln -s /opt/ant/apache-ant-1.9.4 /opt/ant/latest
echo 'ANT_HOME=/opt/ant/latest' >> ~/.bashrc
echo 'PATH=$PATH:$ANT_HOME/bin' >> ~/.bashrc

echo ""
echo "$(tput setaf 2)Finished installing Ant $(tput sgr 0)"
echo ""