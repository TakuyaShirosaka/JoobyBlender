package app.service

import app.common.Const
import app.common.Static
import app.utility.BlenderUtility
import app.utility.HttpRequestBuilder.Companion.createRequestBuilder
import app.utility.HttpRequestBuilder.Companion.getHttpClientBuilder
import app.utility.HttpRequestBuilder.Companion.httpRequestCall
import io.jooby.StatusCode
import io.jooby.exception.NotFoundException
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels.newChannel


data class CreateObjectInput(
        val objectType: String,
        val imageData: List<CreateObjectImageData>
)

data class CreateObjectImageData(
        val file_path: String? = "",
        val file_key: String,
        val width: Double,
        val height: Double,
        var local_file_path: String? = "",
        var glb_file_path: String? = ""
)

data class CreateObjectOutput(
        val glbFilePath: MutableList<String>
)

data class BlenderMacroController(val blenderFilePath: String, val imageFilePath: String, val glbFileName: String, val width: Double, val height: Double)

class WebARService : ProcessService {
    override val logger: Logger = LoggerFactory.getLogger(WebARService::class.java)

    /**
     * Blenderを使用したGLBファイルの生成
     * @args param CreateObjectInput
     * @args imageFiles List<CreateObjectImageData>
     *
     */
    fun createARObject(param: CreateObjectInput, imageData: List<CreateObjectImageData>): List<CreateObjectImageData> {
        val arMaterials = BlenderUtility.getARMaterials(param.objectType)
        return create(arMaterials = arMaterials, imageFiles = imageData)
    }

    /** Blender、Pythonマクロの呼び出し実行部 */
    private fun create(arMaterials: BlenderUtility.ARMaterials, imageFiles: List<CreateObjectImageData>): List<CreateObjectImageData> {
        logger.info("WebARService-create-Start")
        imageFiles.forEach { it ->
            /* GLBファイルの命名と格納パスの生成 */
            val glbFileName = (BlenderUtility.glbFileExportPath + getGLBFileName(File(it.local_file_path).name)).apply {
                it.glb_file_path = this
            }

            /* UI上はcm単位での入力の為、mに変換 */
            val modelSize = BlenderUtility.ModelSize(width = (it.width / 100.0), height = (it.height / 100.0))
            val param = BlenderMacroController(
                    blenderFilePath = arMaterials.blenderFilePath,
                    imageFilePath = it.local_file_path!!,
                    glbFileName = glbFileName,
                    width = modelSize.width,
                    height = modelSize.height
            )

            /* 外部プロセスでBlenderコマンド、Pythonマクロの実行 */
            ProcessBuilder(
                    BlenderUtility.BLENDER_COMMAND,
                    "-b",
                    "-P",
                    arMaterials.pythonFilePath,
                    "--",
                    param.blenderFilePath,
                    param.imageFilePath,
                    param.glbFileName,
                    param.width.toString(),
                    param.height.toString()
            ).let {
                super.processRun(it)
            }
        }
        logger.info("WebARService-create-End")
        return imageFiles
    }

    fun getImageFile(input: List<CreateObjectImageData>, httpClient: OkHttpClient = getHttpClientBuilder().build()): List<CreateObjectImageData> {
        logger.info("WebARService-getImageFile-Start")

        repeat(input.size) { index ->
            val url = URL(input[index].file_path)

            logger.info("WebARService-getImageFile-url-${url}")
            httpRequestCall(httpClient, (createRequestBuilder().url(url).build())).use { response ->
                logger.info("WebARService-getImageFile-response.code-${response.code}")
                if (response.code != StatusCode.OK_CODE) {
                    throw NotFoundException("WebARService-getImageFile failure:${url}")
                }
            }

            val path = url.path
            val fileName = path.substring(path.lastIndexOf("/") + 1)
            var size = 0L;

            newChannel(url.openStream()).use { bc ->
                FileOutputStream(Const.env.getString("tmp.imageFilePath") + fileName).channel.use { fc ->
                    //ファイルチャネルへデータを転送する
                    size = fc.transferFrom(bc, 0, Long.MAX_VALUE)
                }
            }

            input[index].local_file_path = Const.env.getString("tmp.imageFilePath") + fileName
            println("$fileName - $size bytes")
        }

        logger.info("WebARService-getImageFile-End")
        return input
    }

    private fun getGLBFileName(fileName: String): String {
        return "${Static.suffix(fileName)}.glb"
    }

}
