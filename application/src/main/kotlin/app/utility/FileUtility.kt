package app.utility

import app.common.Const
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path

class FileUtility {

    private val logger: Logger = LoggerFactory.getLogger(FileUtility::class.java)

    companion object {
        private val tmpImageFilePath = Const.env.getString("tmp.imageFilePath")

        /**
         * File一括削除
         */
        fun deleteLocalFile(localStoragePath: MutableList<String>) {
            runCatching {
                localStoragePath.forEach {
                    Files.delete(Path.of(it))
                }
            }.fold(
                    onSuccess = {},
                    onFailure = {}
            )
        }
    }
}
