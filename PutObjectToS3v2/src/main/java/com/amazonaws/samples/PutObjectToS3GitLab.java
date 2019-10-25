package com.amazonaws.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class PutObjectToS3GitLab {
	private static final Logger LOGGER = LoggerFactory.getLogger(PutObjectToS3GitLab.class);

	private static S3Client s3Client;

	public static void main(String[] main) throws IOException {
		
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
		s3Client = S3Client.builder().credentialsProvider(ProfileCredentialsProvider.create(profile))
				.region(Region.US_EAST_1).build();

		/** Generating random Universally Unique Identifier */
		UUID uuid = UUID.randomUUID();

		/**
		 * Path to store the file on the S3 Bucket
		 */
		String key = p.getProperty("s3.KEY") + System.currentTimeMillis() + "_" + uuid + ".json.gz";

		/** Bucket */
		String bucketName = p.getProperty("aws.BUCKET_NAME");
		
		/** Data, taking it as a Json format data*/
		String data = ReadingText.readFile();

		try {
			String response = writeToS3(bucketName, key, data);
			LOGGER.info(response);
		} catch (Exception e) {
			String errorStr = "writeToS3: Error writing data to S3, key =  " + key + " ,  bucket = " + bucketName;
			LOGGER.error(errorStr, e);
		}

	}

	/** method to write on the S3 bucket */
	private static String writeToS3(String bucket, String key, String data) throws Exception {

		GZIPOutputStream gzipOutputStream = null;
		PutObjectResponse response = null;
		try {

			/* NOT used from GitLab code: if (key.endsWith(".gz")) { */
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			gzipOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
			gzipOutputStream.finish();

			byte[] zippedBytes = byteArrayOutputStream.toByteArray();

			/** variable to store putObject() response */
			response = s3Client.putObject(
					PutObjectRequest.builder().bucket(bucket).key(key).acl(ObjectCannedACL.PRIVATE).build(),
					RequestBody.fromBytes(zippedBytes));

			/* NOT used from GitLab code:
			 * } else { s3Client.putObject(bucket, key, data); }
			 */
		} catch (Exception e) {
			String errorStr = "writeToS3: Error writing data to S3, key =  " + key + " ,  bucket = " + bucket;
			LOGGER.error(errorStr, e);
			throw e;
		} finally {
			if (gzipOutputStream != null) {
				gzipOutputStream.close();
			}
		}

		if (response.sdkHttpResponse().isSuccessful())
			return "File: \"" + key+ "\" Uploaded Successfully, Status Code: " + response.sdkHttpResponse().statusCode();

		else
			return "File: \"" + key+ "\"  Upload Failed, Status Code: " + response.sdkHttpResponse().statusCode();
	}
}

// NOT used from GitLab code:
//ObjectMetadata metadata = new ObjectMetadata();
//metadata.setContentLength(zippedBytes.length);

// s3.putObject(new PutObjectRequest(bucket, key, new
// ByteArrayInputStream(zippedBytes), null)); // WORKED BUT GAVE WARNINGS