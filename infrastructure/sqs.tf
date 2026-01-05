terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  access_key = "test"
  secret_key = "test"
  region     = "us-east-1"

  # ⚠️ This is the magic part: Force Terraform to talk to LocalStack
  endpoints {
    sqs = "http://localhost:4566"
    # Add other services here if needed (s3, dynamodb, etc.)
  }

  # Skip validation checks that fail locally
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true
}

resource "aws_sqs_queue" "my_local_queue" {
  name                      = "user-queue"
  delay_seconds             = 90
  max_message_size          = 2048
  message_retention_seconds = 86400
  receive_wait_time_seconds = 10
}