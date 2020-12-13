package app.utility

import app.common.Const
import com.amazonaws.AmazonServiceException
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.AmazonS3Exception
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL


class AmazonS3Utility {
    private val logger: Logger = LoggerFactory.getLogger(AmazonS3Utility::class.java)
    private val s3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(
                    EndpointConfiguration(Const.Aws.s3EndpointUrl, Const.Aws.regions)
            )
            .withPathStyleAccessEnabled(true)
            .build()

    data class S3UploadInput(val bucketName: String, val uploadData: UploadData)
    data class UploadData(val fileKey: String, val localFilePath: String)

    /**
     * s3Upload
     * @return fileDownLoadPath:S3へ格納したFileのダウンロードURL
     */
    fun s3Upload(input: S3UploadInput): String {
        try {
            val s3BucketBaseUrl = s3GetFolderFilePath(input.bucketName, input.uploadData.fileKey)
            s3.putObject(input.bucketName, "${input.uploadData.fileKey}/${File(input.uploadData.localFilePath).name}", File(input.uploadData.localFilePath))
            return "${s3BucketBaseUrl}/${File(input.uploadData.localFilePath).name}"
        } catch (e: Exception) {
            when (e) {
                is AmazonServiceException -> logger.error("AmazonServiceException:$e")

                is AmazonS3Exception -> logger.error("AmazonS3Exception:$e")
                else -> logger.error("Exception:$e")
            }
            throw e
        }
    }

    private fun s3GetFolderFilePath(bucketName: String, bucketKey: String): URL? {
        return s3.getUrl(bucketName, bucketKey)
    }
}