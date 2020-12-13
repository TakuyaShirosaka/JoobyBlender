#!/usr/bin/env bash
echo "storage_init START!"

aws --region ap-northeast-1 --endpoint-url=http://localstack:4566 s3 mb s3://ar-bucket
aws --endpoint-url=http://localstack:4566 s3api put-bucket-acl --bucket ar-bucket --acl public-read

echo "storage_init END!"











