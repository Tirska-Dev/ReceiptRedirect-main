#!/bin/bash
sam build && sam deploy --stack-name RedirectFunction --s3-bucket receipt.com.ua --region eu-central-1 --profile receipt.com.ua --capabilities CAPABILITY_IAM