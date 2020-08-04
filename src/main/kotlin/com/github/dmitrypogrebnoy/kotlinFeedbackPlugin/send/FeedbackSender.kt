package com.github.dmitrypogrebnoy.kotlinFeedbackPlugin.send

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler
import org.apache.commons.httpclient.Header
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.apache.commons.httpclient.methods.multipart.FilePart
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity
import org.apache.commons.httpclient.params.HttpConnectionParams
import org.apache.commons.httpclient.params.HttpMethodParams
import java.io.File
import java.io.IOException

object FeedbackSender {

    private const val repositoryApiUrl: String = "https://k-feedback-plugin-test.myjetbrains.com/youtrack/api"
    private const val feedbackProjectId: String = "0-0"
    private const val userAgent: String = "Kotlin Feedback plugin"

    // In ms
    private const val connectionTimeout: Int = 30000

    // In ms
    private const val socketTimeout: Int = 10000

    // Size in byte - 10 Mb
    const val attachFileMaxSize: Int = 10485760

    private val httpClient: HttpClient = HttpClient()

    init {
        httpClient.params.setParameter(HttpMethodParams.RETRY_HANDLER, DefaultHttpMethodRetryHandler())
        httpClient.params.setParameter(HttpMethodParams.USER_AGENT, userAgent)
        httpClient.params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, connectionTimeout)
        httpClient.params.setParameter(HttpConnectionParams.SO_TIMEOUT, socketTimeout)
    }

    //Returns the created issue id or throws an IOException
    fun createFeedbackIssue(subject: String, description: String): String {
        val createIssueMethod = PostMethod("$repositoryApiUrl/issues")
        createIssueMethod.addRequestHeader(Header("Accept", "application/json"))
        createIssueMethod.addRequestHeader(Header("Authorization", "Bearer ${System.getenv("YouTrackPermanentToken")}"))
        val youTrackFeedbackProjectId = JsonObject().apply {
            addProperty("id", feedbackProjectId)
        }
        val feedbackIssueJson = JsonObject().apply {
            add("project", youTrackFeedbackProjectId)
            addProperty("summary", subject)
            addProperty("description", description)
        }.toString()
        createIssueMethod.requestEntity = StringRequestEntity(
                feedbackIssueJson,
                "application/json",
                Charsets.UTF_8.name()
        )
        try {
            val statusCode = httpClient.executeMethod(createIssueMethod)
            val responseBody = createIssueMethod.responseBodyAsStream.reader().readText()
            if (statusCode != HttpStatus.SC_OK) {
                val errorResponse = JsonParser.parseString(responseBody).asJsonObject
                throw IOException("Error creating issue: ${errorResponse["error"]}," +
                        " ${errorResponse["error_description"]}")
            }
            val responseBodyJson = JsonParser.parseString(responseBody).asJsonObject
            createIssueMethod.releaseConnection()
            return responseBodyJson["id"].asString
        } finally {
            createIssueMethod.releaseConnection()
        }
    }

    //Returns the attached file id or throws an IOException
    fun attachFileToIssue(issueId: String, file: File): String {
        val attachFileMethod = PostMethod("$repositoryApiUrl/issues/${issueId}/attachments?fields=id")
        attachFileMethod.addRequestHeader(Header("Accept", "application/json"))
        attachFileMethod.addRequestHeader(Header("Authorization", "Bearer ${System.getenv("YouTrackPermanentToken")}"))
        attachFileMethod.params.setParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true)
        val filePart = FilePart(file.name, file)
        attachFileMethod.requestEntity = MultipartRequestEntity(arrayOf(filePart), attachFileMethod.params)
        try {
            val statusCode = httpClient.executeMethod(attachFileMethod)
            val responseBody = attachFileMethod.responseBodyAsStream.reader().readText()
            if (statusCode != HttpStatus.SC_OK) {
                val errorResponse = JsonParser.parseString(responseBody).asJsonObject
                throw IOException("Error attaching file to issue: ${errorResponse["error"]}," +
                        " ${errorResponse["error_description"]}")
            }
            val responseBodyJson = JsonParser.parseString(responseBody).asJsonArray
            attachFileMethod.releaseConnection()
            return responseBodyJson.single().asJsonObject["id"].toString()
        } finally {
            attachFileMethod.releaseConnection()
        }
    }
}