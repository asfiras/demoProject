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

#copy artifacts to S3
awslocal s3 cp /home/firas/Firas/demoProject/first-app/target/first-app-1.0-SNAPSHOT-jar-with-dependencies.jar s3://release-bucket/first-app-1.0-SNAPSHOT.jar
awslocal s3 cp /home/firas/Firas/demoProject/second-app/target/second-app-1.0-SNAPSHOT-jar-with-dependencies.jar s3://release-bucket/second-app-1.0-SNAPSHOT.jar

#create docker image from docker files

sudo docker build -t app1 /home/firas/Firas/demoProject/CI-CD/App1-dockerfile/ --network="host" --no-cache
sudo docker build -t app2 /home/firas/Firas/demoProject/CI-CD/App2-dockerfile/ --network="host" --no-cache

#Setup Aws infrastructure using Terraform
cd /home/firas/Firas/demoProject/Terraform
tflocal plan
tflocal apply

#deploy Apps to docker Daemon
sudo docker run -i -t -d app1
sudo docker run -i -t -d app2

#sudo docker container exec -it $ java -jar /first-app-1.0-SNAPSHOT.jar
#sudo docker container ls
# sudo docker run app1 find / -name first-app-1.0-SNAPSHOT.jar
