package com.android_abel.indah.service.remote.base

import android.util.Base64
import com.android_abel.indah.utils.base.Base64.encodeBytes
import com.android_abel.indah.utils.base.CryptoUtils.getSecret
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ServiceGenerator {
    //public static final String BASIC_SECURITY_HEADER = "basic_security_header";
//public static final String BASIC_SECURITY_VALUE = "4QJzQ%animation_dia_despejado'B#-qZ/eK8sk;2pAzj>=animation_dia_lloviendo`u5Ec%.cyx}`hy<G!:]&<V3s}i*A)2t]zyG";
    fun <S> createService(
        baseUrl: String,
        basicSecurityHeader: String?,
        basicSecurityValue: String?,
        serviceClass: Class<S>
    ): S { //region example convert date time
//basic date format
//Gson gson = new GsonBuilder().setDateFormat(MessageServices.DEFAULT_FORMAT_DATETIME).setLenient().create();
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Date::class.java,
                GsonSerializers.serializerDateToTimeLong
            ) //.registerTypeAdapter(Date.class, GsonSerializers.deserializerDateToTimeLong)
            .create()
        //endregion
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                getUnsafeClientBuilder(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.HEADERS)
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                    , 10,
                    basicSecurityHeader,
                    basicSecurityValue
                )
            )
            .build()
        return retrofit.create(serviceClass)
    }

    fun getUnsafeClientBuilder(
        interceptor: HttpLoggingInterceptor?,
        timeout: Int,
        basicSecurityHeader: String?,
        basicSecurityValue: String?
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return try {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val clientBuilder = builder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(9999, TimeUnit.SECONDS)
                .writeTimeout(9999, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            addBasicSecurity(
                clientBuilder,
                basicSecurityHeader,
                basicSecurityValue
            )
            clientBuilder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun addBasicSecurity(
        clientBuilder: OkHttpClient.Builder,
        basicSecurityHeader: String?,
        basicSecurityValue: String?
    ): OkHttpClient.Builder {
        if (basicSecurityHeader != null && !basicSecurityHeader.isEmpty()) {
            clientBuilder.addInterceptor { chain: Interceptor.Chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader(
                        basicSecurityValue,
                        basicSecurityValue
                    ) //.addHeader("deviceplatform", "android")
//.removeHeader("User-Agent")
//.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                    .build()
                val response = chain.proceed(request)
                response
            }
        }
        return clientBuilder
    }

    fun getUnsafeClientBuilder(timeout: Int): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return try {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val clientBuilder = builder
                .connectTimeout(9999, TimeUnit.SECONDS)
                .readTimeout(9999, TimeUnit.SECONDS)
                .writeTimeout(9999, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            clientBuilder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun generateBasicAuthToken(phoneNumber: String): String { /* from https://github.com/BlinkItSolutions/mobile-api/wiki/Accounts-API-Protocol
         *  basic_auth are the authorization credentials the client would like to create.
         *  These are in the form of Base64({number}:{password}),
         *  where number is the client's verified PSTN number and password is animation_dia_despejado randomly generated 16 byte ASCII string,
         *  this credentials are used in basic auth for validate the device.
         * */
        val password =
            getSecret(16) //new RandomString(16, new Random()).nextString();
        val token = "$phoneNumber:$password"
        val encoded =
            Base64.encodeToString(token.toByteArray(), Base64.DEFAULT)
        return encoded.replace("\\n".toRegex(), "")
    }

    fun generateBasicAuthToken(phoneNumber: String, password: String): String {
        val token = "$phoneNumber:$password"
        val encoded =
            Base64.encodeToString(token.toByteArray(), Base64.DEFAULT)
        return encoded.replace("\\n".toRegex(), "")
    }

    fun generateAESKey(): String? {
        var key: String? = null
        var secretKey: SecretKey? = null
        var keyGen: KeyGenerator? = null
        try {
            keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(32)
            secretKey = keyGen.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        if (secretKey != null) {
            key = encodeBytes(secretKey.encoded)
        }
        return key
    }

    fun generateSha1Mac(s: String, keyString: String): String {
        var bytes: ByteArray? = null
        try {
            val key = SecretKeySpec(
                keyString.toByteArray(charset("UTF-8")),
                "HmacSHA1"
            )
            var mac: Mac? = null
            mac = Mac.getInstance("HmacSHA1")
            mac.init(key)
            bytes = mac.doFinal(s.toByteArray(charset("UTF-8")))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        return bytes?.let { String(it) } ?: ""
    }
}