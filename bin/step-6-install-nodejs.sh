#!/bin/bash

echo ""
echo "$(tput setaf 2)Installing NodeJS and related tools$(tput sgr 0)"
echo ""

curl -sL https://rpm.nodesource.com/setup | bash -
yum install -y nodejs gcc-c++ make ruby ruby-devel rubygems libpng-devel
gem update --system --no-document  && gem install compass

echo ""
echo "$(tput setaf 2)Updating NPM $(tput sgr 0)"
echo ""
npm install -g npm@latest

echo ""
echo "$(tput setaf 2)Installing Yeoman and related tools $(tput sgr 0)"
echo ""
npm install --global yo bower grunt-cli

echo ""
echo "$(tput setaf 2)Finished installing $(tput sgr 0)"
echo ""
