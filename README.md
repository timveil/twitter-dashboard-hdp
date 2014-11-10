1. clone repo locally for future development
```
git clone https://github.com/timveil/twitter-dashboard-hdp.git
```

1. install sandbox locally with `bin/install-sandbox.sh`
1. enable ambari and confirm all serivces are up and running (including storm)
  1. http://sandbox.hortonworks.com:8000/about/ > click enable
  1. http://sandbox.hortonworks.com:8080/#/main/dashboard > start storm, make sure it comes up cleanly
1. ssh into the sandbox
```
ssh root@sandbox.hortonworks.com -p 2222
```
1. once in the sandbox run the following commands
```
# clone the demo
git clone https://github.com/timveil/twitter-dashboard-hdp.git

# navigate to the demo bin directory
cd twitter-dashboard-hdp/bin

# install required software
./step-0-install-all.sh

# start the demo
./start-demo.sh
```
1.  if everything goes smoothly, head to http://sandbox.hortonworks.com:8181/

