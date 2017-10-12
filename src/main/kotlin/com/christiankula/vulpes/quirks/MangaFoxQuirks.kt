package com.christiankula.vulpes.quirks

import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class MangaFoxQuirks private constructor() {
    companion object {
        /**
         * Basically, MangaFox rejects HTTP requests that occur too close to each other (HTTP code 403),
         * thus fetching page counts can't be parallelized and a small delay between requests is necessary.
         * Same applies for page downloading, thus making MangaFox crawling very slow.
         */
        val DELAY_BETWEEN_HTTP_REQUESTS_MS: Long = 200

        fun ignoreInvalidSslCertificates() {
            val a = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null // Not relevant.
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
                    // Do nothing. Just allow them all.
                }

                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
                    // Do nothing. Just allow them all.
                }
            }
            val trustAllCertificates = arrayOf(a)

            val trustAllHostnames = object : HostnameVerifier {
                override fun verify(hostname: String, session: SSLSession): Boolean {
                    return true // Just allow them all.
                }
            }

            try {
                System.setProperty("jsse.enableSNIExtension", "false")
                val sc = SSLContext.getInstance("SSL")
                sc.init(null, trustAllCertificates, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
                HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames)
            } catch (e: GeneralSecurityException) {
                throw ExceptionInInitializerError(e)
            }
        }
    }
}