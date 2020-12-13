package app.utility

import app.common.Const
import okhttp3.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class HttpRequestBuilder {
    companion object {
        private val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
        )

        /** OoHttpClientのBuilderを返却する、開発/テストではSSLの検証をスキップ  */
        fun getHttpClientBuilder(): OkHttpClient.Builder {
            if (Const.envName == "prod") return OkHttpClient.Builder()

            /* 証明書の検証を全て通過するようにする */
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder
        }

        /** ヘッダー情報、共通的なパラメータを付与して返却 */
        fun createRequestBuilder(): Request.Builder {
            return Request.Builder()
        }

        /** httpリクエスト実行 */
        fun httpRequestCall(httpClient: OkHttpClient, request: Request): Response {
            return httpClient.newCall(request).execute()
        }

    }


}