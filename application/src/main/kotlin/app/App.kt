package app

import app.common.Const
import app.route.WebAR
import io.jooby.*
import io.jooby.json.JacksonModule


class App : Kooby({
    /** AccessLog用 **/
    decorator(AccessLogHandler())

    /** Jacksonモジュール、ここで読み込まないと効果がない */
    install(JacksonModule())

    /** CORS用のハンドラー、ここで読み込まないと効果がない */
    val cors = Cors.from(config)
    decorator(CorsHandler(cors))

    get("/") {
        Const.env.getString("tmp.imageFilePath")
    }

    use("/WebAR", WebAR())
})

fun main(args: Array<String>) {
    runApp(args, App::class)
}
