#!/bin/bash

#curl "http://hortonassets.s3.amazonaws.com/2.1/virtualbox/Hortonworks_Sandbox_2.1.ova" -o "Hortonworks_Sandbox_2.1.ova"

HDPNAME="Hortonworks Sandbox 2.1 - Twitter Demo"

VBoxManage import Hortonworks_Sandbox_2.1.ova --vsys 0 --vmname "$HDPNAME" --vsys 0 --memory 8128 --vsys 0 --cpus 4

VBoxManage modifyvm "$HDPNAME" --natpf1 storm1,tcp,127.0.0.1,9092,,9092
VBoxManage modifyvm "$HDPNAME" --natpf1 storm2,tcp,127.0.0.1,8005,,8005
VBoxManage modifyvm "$HDPNAME" --natpf1 solr,tcp,127.0.0.1,8983,,8983
VBoxManage modifyvm "$HDPNAME" --natpf1 jetty,tcp,127.0.0.1,8181,,8181
VBoxManage modifyvm "$HDPNAME" --natpf1 hbase,tcp,127.0.0.1,60010,,60010

VBoxManage startvm "$HDPNAME"

