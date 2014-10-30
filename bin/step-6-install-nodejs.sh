#!/bin/bash

echo ""
echo "$(tput setaf 2)Installing NodeJS and related tools$(tput sgr 0)"
echo ""

curl -sL https://rpm.nodesource.com/setup | bash -
yum install -y nodejs

echo ""
echo "$(tput setaf 2)Updating NPM $(tput sgr 0)"
echo ""
npm install -g npm@latest

echo ""
echo "$(tput setaf 2)Installing Yeoman $(tput sgr 0)"
echo ""
npm install --global yo bower grunt-cli

echo ""
echo "$(tput setaf 2)Finished installing $(tput sgr 0)"
echo ""
