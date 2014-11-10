1) clone repo locally for future development
    ```
    git clone https://github.com/timveil/twitter-dashboard-hdp.git
    ```

1) install sandbox locally with `bin/install-sandbox.sh`
2) install ambari and confirm all serivces are up and running
    http://sandbox.hortonworks.com:8000/about/ > enable
    http://sandbox.hortonworks.com:8080/#/main/dashboard

3) then do below



ssh root@sandbox.hortonworks.com -p 2222

git clone https://github.com/timveil/twitter-dashboard-hdp.git

cd twitter-dashboard-hdp/bin

./step-0-install-all.sh



./start-demo.sh

http://sandbox.hortonworks.com:8181/


to delete solr index

curl http://sandbox.hortonworks.com:8983/solr/tweets/update?commit=true -d  '<delete><query>*:*</query></delete>'

Please see the [wiki](https://github.com/timveil/twitter-dashboard-hdp/wiki) for documentation