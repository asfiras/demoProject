##
## Main configuration: creates several SQS queues using a module, then 
## combines their policies into an application role.
## 

provider "aws" {
  access_key                  = "test"
  secret_key                  = "test"
  region                      = "us-east-1"
  s3_force_path_style         = false
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    apigateway     = "http://localhost:4566"
    apigatewayv2   = "http://localhost:4566"
    cloudformation = "http://localhost:4566"
    cloudwatch     = "http://localhost:4566"
    dynamodb       = "http://localhost:4566"
    ec2            = "http://localhost:4566"
    es             = "http://localhost:4566"
    elasticache    = "http://localhost:4566"
    firehose       = "http://localhost:4566"
    iam            = "http://localhost:4566"
    kinesis        = "http://localhost:4566"
    lambda         = "http://localhost:4566"
    rds            = "http://localhost:4566"
    redshift       = "http://localhost:4566"
    route53        = "http://localhost:4566"
    s3             = "http://s3.localhost.localstack.cloud:4566"
    secretsmanager = "http://localhost:4566"
    ses            = "http://localhost:4566"
    sns            = "http://localhost:4566"
    sqs            = "http://localhost:4566"
    ssm            = "http://localhost:4566"
    stepfunctions  = "http://localhost:4566"
    sts            = "http://localhost:4566"
  }
}

module "App1_q" {
  source = "./modules/sqs"
  queue_name = "App1_q"
}


module "App2_q" {
  source = "./modules/sqs"
  queue_name = "App2_q"
}


resource "aws_iam_role" "application_role" {
  name = "ApplicationRole"

  assume_role_policy = jsonencode({
    "Version"   = "2012-10-17",
    "Statement" = [
      {
        "Effect"    = "Allow"
        "Action"    = "sts:AssumeRole"
        "Principal" = { "Service" = "ec2.amazonaws.com" }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "application_role_notifications_producer" {
  role       = aws_iam_role.application_role.name
  policy_arn = module.App1_q.producer_policy_arn
}

resource "aws_iam_role_policy_attachment" "application_role_rendering_producer" {
  role       = aws_iam_role.application_role.name
  policy_arn = module.App2_q.producer_policy_arn
}