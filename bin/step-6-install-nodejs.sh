#!/bin/bash

echo ""
echo "$(tput setaf 2)Installing NodeJS $(tput sgr 0)"
echo ""

curl -sL https://rpm.nodesource.com/setup | bash -
yum install -y nodejs

echo ""
echo "$(tput setaf 2)Finished installing NodeJS $(tput sgr 0)"
echo ""
