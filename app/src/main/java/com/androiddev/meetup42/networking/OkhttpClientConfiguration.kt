package com.androiddev.meetup42.networking

import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


private const val TLS_VERSION = "TLSv1.2"
private const val CERTIFICATE_INSTANCE_TYPE = "X.509"

/**
 * @author Freddy Lazo.
 */
@Throws(RuntimeException::class)
internal fun getSecureOkHttpClient(caInput: InputStream): OkHttpClient.Builder {
    try {
        val certificateFactory: CertificateFactory =
            CertificateFactory.getInstance(CERTIFICATE_INSTANCE_TYPE)
        val certificateAuthority: X509Certificate = caInput.use {
            certificateFactory.generateCertificate(it) as X509Certificate
        }

        // Create a KeyStore containing our trusted CAs
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", certificateAuthority)
        }

        // Create a TrustManager that trusts the CAs inputStream our KeyStore
        val trustManagerFactoryAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(trustManagerFactoryAlgorithm).apply {
                init(keyStore)
            }

        val trustManagers = trustManagerFactory.trustManagers

        // Create an SSLContext that uses our TrustManager
        val context: SSLContext = SSLContext.getInstance(TLS_VERSION).apply {
            init(null, trustManagers, null)
        }

        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(trustManagers)
        }
        val trustManager = trustManagers[0] as X509TrustManager
        return OkHttpClient.Builder().sslSocketFactory(context.socketFactory, trustManager)
    } catch (exception: Exception) {
        throw RuntimeException(exception)
    }
}