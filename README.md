# DevOps Demo Project

this Project is for Demo and testing purpose

## Content
- *CICD* : includs script for cicd pipeline and docker files for App1 and App2
- *first-App* : includes App1 Maven project which send to and recieve massages from App2 via sqs
- *second-App* : includes App2 Maven project which send to and recieve massages from App1 via sqs
- *Terraform* : contain project infrastucture as a code to deply App infrastructure

# Important Note
this app is using localstack to simulate AWS please visit this page for more info : 
https://docs.localstack.cloud/get-started/

# needed packages
- Maven
- localstack with awslocal and tflocal setup
- Terraform
- AWS CLI V2
- tflint
- Docker Dameon 
