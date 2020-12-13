package app.service

import org.slf4j.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

interface ProcessService {
    val logger: Logger
    fun processRun(pb: ProcessBuilder) {
        val process = pb.start()
        val ret = process.waitFor()
        return run {
            logger.info("★Ret:${ret}")
            this.dispProcessLog(process)
            if (ret != 0) {
                throw Exception("${pb.command()} is failed")
            }
        }
    }


    /**
     * 外部プロセスの実行ログを表示
     */
    data class Console(val log: BufferedReader, val error: BufferedReader)

    fun dispProcessLog(process: Process): Unit {
        process.run {
            val (log, error) =
                    Console(log = BufferedReader(InputStreamReader(inputStream)),
                            error = BufferedReader(InputStreamReader(errorStream)))
            logger.info("★Log:")
            while (true) {
                println(log.readLine() ?: break)
            }
            logger.info("★Error:")
            while (true) {
                println(error.readLine() ?: break)
            }
        }
    }

}
