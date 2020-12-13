package app.route

import app.common.Const
import app.service.*
import app.utility.AmazonS3Utility
import app.utility.FileUtility
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jooby.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WebAR : Kooby({

    //DB周りのモジュールインストール
    val mapper = jacksonObjectMapper()
    val amazonUtility = AmazonS3Utility()
    val service = WebARService()
    val logger: Logger = LoggerFactory.getLogger(WebAR::class.java)

    post("/createARObject") { ctx ->
        val input: CreateObjectInput = ctx.body<CreateObjectInput>()

        runBlocking {
            runCatching {
                //ARモデル生成
                val containGlbFilePath: List<CreateObjectImageData> =
                        withContext(Dispatchers.Default) {
                            service.getImageFile(input = input.imageData).let { imageFile_completed ->
                                return@withContext service.createARObject(input, imageFile_completed)
                            }
                        }

                val s3glbFilesUploadPath: Deferred<MutableList<String>> = async {
                    val uploadGlbFilePath = mutableListOf<String>()
                    containGlbFilePath.forEach { image ->
                        uploadGlbFilePath.add(amazonUtility.s3Upload(
                                AmazonS3Utility.S3UploadInput(
                                        bucketName = Const.Aws.s3BucketName["1"] ?: error(""),
                                        uploadData = AmazonS3Utility.UploadData(
                                                fileKey = image.file_key,
                                                localFilePath = image.glb_file_path!!)))
                        )
                    }
                    return@async uploadGlbFilePath
                }

                ctx.run {
                    responseCode = StatusCode.OK
                    setResponseHeader("content-type", MediaType.json)
                    mapper.writeValueAsString(CreateObjectOutput(glbFilePath = s3glbFilesUploadPath.await()))
                }

            }.fold(
                    onSuccess = { it },
                    onFailure = {
                        println("SERVER_ERROR")
                        logger.error(it.localizedMessage)
                        ctx.run {
                            responseCode = StatusCode.SERVER_ERROR
                            setResponseHeader("content-type", MediaType.json)
                        }
                    })
                    .also {
                        input.imageData.forEach { image ->
                            FileUtility.deleteLocalFile(mutableListOf(image.local_file_path!!))
                            FileUtility.deleteLocalFile(mutableListOf(image.glb_file_path!!))
                        }
                    }
        }
    }
})
