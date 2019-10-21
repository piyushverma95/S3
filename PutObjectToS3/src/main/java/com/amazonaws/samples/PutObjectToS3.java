package com.amazonaws.samples;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class PutObjectToS3 {

	public static void main(String[] args) {

			// S3Client accessing AWS access key and secret access key from "app-1-development" profile
			S3Client s3Client = S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create("app-1-development"))
					.region(new DefaultAwsRegionProviderChain().getRegion()).build();

			// Bucket Name
			String BUCKET_NAME = "test-new-bu";
			
			// Path to store the file
			String KEY = "2019/Oct21/text.txt";
			
			// ReadingText.readFile() returning String from a file
			String text = ReadingText.readFile();

			// Uploading the file
			s3Client.putObject(PutObjectRequest.builder().bucket(BUCKET_NAME).key(KEY).acl(ObjectCannedACL.PUBLIC_READ).build(),
						RequestBody.fromBytes(text.getBytes()));
			
			System.out.println("File Successfully Uploaded");	
	}

}
