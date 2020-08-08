package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.network

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.params.HttpConnectionParams
import org.apache.commons.httpclient.params.HttpMethodParams

object HttpClient {

    private const val userAgent: String = "Kotlin Feedback plugin"

    // In ms
    private const val connectionTimeout: Int = 30000

    // In ms
    private const val socketTimeout: Int = 10000

    private val httpClient: HttpClient = HttpClient()

    init {
        httpClient.params.setParameter(HttpMethodParams.RETRY_HANDLER, DefaultHttpMethodRetryHandler())
        httpClient.params.setParameter(HttpMethodParams.USER_AGENT, userAgent)
        httpClient.params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, connectionTimeout)
        httpClient.params.setParameter(HttpConnectionParams.SO_TIMEOUT, socketTimeout)
    }

    fun executeMethod(method: HttpMethod): Int {
        return httpClient.executeMethod(method)
    }
}