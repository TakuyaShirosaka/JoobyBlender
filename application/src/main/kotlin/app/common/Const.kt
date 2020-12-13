package app.common

import io.jooby.Kooby

object Const {

    //環境変数取得
    val env = Kooby().environment.config
    val envName = env.getString("env.name") ?: "debug"

    object Aws {
        val regions = env.getString("aws.regions") ?: "ap-northeast-1"

        //AWS-S3
        val s3EndpointUrl = env.getString("aws.s3.endpoint.url") ?: "http://localstack:4566/"
        val s3BucketName = mapOf(
                "1" to (env.getString("aws.s3.arBucket.name") ?: "ar-bucket")
        )
    }

    /**
    作成パターン:	(pattern/photo)
    システム区分:	(wallpaper/sticker/matt/artboard/roll)
    カテゴリ:	(photo/symbol/pattern)
     * */
    object WebAR {

        /* 依存モジュール */
        val pythonFilePath = mapOf(
                "1" to "/pythonMacro/Controller/BlenderMacro_wall_Controller.py"
        )

        val blenderFilePath = mapOf(
                "1" to "/pythonMacro/3dModel/wall_1m.blend"
        )
    }
}