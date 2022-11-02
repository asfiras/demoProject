#!/bin/bash

#Deploy First App
cd /home/firas/Firas/demoProject/first-app
mvn clean compile assembly:single
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "App1 Deployment Successful"
else
echo "App1 Deployment Failed"
fi

#Deploy Second App
cd /home/firas/Firas/demoProject/second-app 
 mvn clean compile assembly:single
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "App2 Deployment Successful"
else
echo "App2 Deployment Failed"
fi

#test main App function by sending test massage and test to recieve it
awslocal sqs send-message --queue-url http://localhost:4566/000000000000/App1_q --message-body "test1234" --delay-seconds 10
check_string=$(awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/App1_q | jq -r '.Messages[] | .Body')
echo $check_string
if [ $check_string = "test1234" ]; then
echo "App Function tested Successful"
else
echo "App Test Failed"
fi

#copy artifacts to S3
awslocal s3 cp /home/firas/Firas/demoProject/first-app/target/first-app-1.0-SNAPSHOT-jar-with-dependencies.jar s3://release-bucket/first-app-1.0-SNAPSHOT.jar
awslocal s3 cp /home/firas/Firas/demoProject/second-app/target/second-app-1.0-SNAPSHOT-jar-with-dependencies.jar s3://release-bucket/second-app-1.0-SNAPSHOT.jar

#create docker image from docker files

sudo docker build -t app1 /home/firas/Firas/demoProject/CI-CD/App1-dockerfile/ --network="host" --no-cache
sudo docker build -t app2 /home/firas/Firas/demoProject/CI-CD/App2-dockerfile/ --network="host" --no-cache

#Setup Aws infrastructure using Terraform
cd /home/firas/Firas/demoProject/Terraform
tflocal plan
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "Terraform Plan done successfully"
else
echo "Terraform Plan failed , you may not proceed tp Apply"
fi
tflocal apply
STATUS=$?
if [ $STATUS -eq 0 ]; then
echo "Terraform Apply done successfully"
else
echo "Terraform Apply failed"
fi

#deploy Apps to docker Daemon
sudo docker run -i -t -d app1
sudo docker run -i -t -d app2

#useful commands

#sudo docker container exec -it $ java -jar /first-app-1.0-SNAPSHOT.jar
#sudo docker container ls
#sudo docker run app1 find / -name first-app-1.0-SNAPSHOT.jar
