package com.amazonaws.samples;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

/** Class to upload an Object on S3 */
public class PutObjectToS3V2 {

	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(PutObjectToS3V2.class.getName());

	/** variable to store putObject() response */
	private static PutObjectResponse response;

	/** variable to store zipped data */
	private static ByteArrayOutputStream byteArrayOutputStream;

	public static void main(String[] args) {
		try {
			String response = uploadFileToS3();
			log.info(response);
		} catch (NullPointerException e) {
			log.info("Response received as Null: " + e);
		}
	}

	/**
	 * method to upload the zip format file on a S3 Bucket
	 */
	private static String uploadFileToS3() {
		try {
			/**
			 * Loading the properties file
			 */
			FileReader reader = new FileReader(
					"D:\\Files On Outlook\\OneDrive - Infosys Limited\\Files\\Spring\\Projects\\PutObjectToS3V2\\src\\main\\resources\\property.properties");
			Properties p = new Properties();
			p.load(reader);

			String profile = p.getProperty("aws.PROFILE");

			/**
			 * S3Client accessing AWS access key and secret access key from the profile
			 */
			S3Client s3Client = S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create(profile))
					.region(Region.US_EAST_1).build();

			/**
			 * Path to store the file on the S3 Bucket
			 */
			String key = p.getProperty("s3.KEY");

			/** Bucket */
			String bucketName = p.getProperty("aws.BUCKET_NAME");

			/** Zip String data */
			zipFile();

			/** Generating random Universally Unique Identifier */
			UUID uuid = UUID.randomUUID();

			/**
			 * Uploading the Object in zip format, the file permission is private
			 */
			response = s3Client.putObject(
					PutObjectRequest.builder().bucket(bucketName)
							.key(key + System.currentTimeMillis() + "_" + uuid + ".json.gz")
							.acl(ObjectCannedACL.PRIVATE).build(),
					RequestBody.fromBytes(byteArrayOutputStream.toByteArray()));

		} catch (SdkClientException e) {
			log.info("Incorrect Profile Selected: " + e);
		} catch (NoSuchBucketException e) {
			log.info("Incorrect Bucket Name: " + e);
		} catch (SdkException e) {
			log.info("Exception Occured: " + e);
		} catch (FileNotFoundException e) {
			log.info("File Not Found: " + e);
		} catch (IOException e) {
			log.info("Exception Occured: " + e);
		}

		/**
		 * sdkHttpResponse() returns HTTP response data returned from the service
		 * isSuccessful() returns true if the object is successfully uploaded
		 * statusCode() returns success code 200 if an object is successfully uploaded
		 * on the S3 Bucket
		 */
		if (response.sdkHttpResponse().isSuccessful())
			return "File Uploaded Successfully, Status Code: " + response.sdkHttpResponse().statusCode();

		else
			return "File Upload Failed, Status Code: " + response.sdkHttpResponse().statusCode();
	}

	/** method to store string data in zip format */
	public static void zipFile() {
		GZIPOutputStream gzipOut;
		String text = ReadingText.readFile();
		/** writing compressed data in GZIP file format */
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			gzipOut = new GZIPOutputStream(byteArrayOutputStream);
			gzipOut.write(text.toString().getBytes());
			gzipOut.finish();
		} catch (IOException e) {
			log.info("Exception Occured: " + e);
		}
	}
}
