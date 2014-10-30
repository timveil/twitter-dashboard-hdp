#!/bin/bash

echo ""
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo "$(tput setaf 2)###### Step 6 - Install NodeJS, etc $(tput sgr 0)"
echo "$(tput setaf 2)######################################### $(tput sgr 0)"
echo ""

echo ""
echo "$(tput setaf 2)##### Installing NodeJS and related tools$(tput sgr 0)"
echo ""

curl -sL https://rpm.nodesource.com/setup | bash -
yum -q install -y -t nodejs
yum -q install -y -t gcc-c++ make ruby ruby-devel rubygems libpng-devel
gem update --quiet --system
gem install compass --quiet --no-document

echo ""
echo "$(tput setaf 2)##### Updating NPM $(tput sgr 0)"
echo ""

npm config set loglevel win --global

npm install -g npm@latest

echo ""
echo "$(tput setaf 2)##### Installing Yeoman and related tools $(tput sgr 0)"
echo ""
npm install --loglevel win --g yo --no-color --no-insight --ignore-scripts
npm install --g bower
npm install --g grunt-cli

echo ""
echo "$(tput setaf 2)##### Finished installing $(tput sgr 0)"
echo ""
