#!/bin/bash

echo ""
echo "$(tput setaf 2)Installing NodeJS $(tput sgr 0)"
echo ""

curl -sL https://rpm.nodesource.com/setup | bash -
yum install -y nodejs


npm install -g npm@latest
npm install -g yo

echo ""
echo "$(tput setaf 2)Finished installing NodeJS $(tput sgr 0)"
echo ""

