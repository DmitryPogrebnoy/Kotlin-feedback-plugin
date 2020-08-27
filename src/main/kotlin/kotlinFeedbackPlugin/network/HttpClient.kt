package kotlinFeedbackPlugin.network

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.params.HttpConnectionParams
import org.apache.commons.httpclient.params.HttpMethodParams
import java.util.*

/**
 * HTTP client for all plugin requests.
 */
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

    /**
     * Load json file from open Github repository.
     *
     * @param url the path to the file in the Github repository.
     * @see kotlinFeedbackPlugin.network.FunctionalitySettingsLoader.jsonSettings
     */
    fun getJsonFileFromGithub(url: String): JsonObject? {
        val getJsonFileMethod = GetMethod(url)
        getJsonFileMethod.addRequestHeader(Header("Accept:", "application/vnd.github.v3+json"))
        //getJsonFileMethod.addRequestHeader(Header("Authorisation:", "token ${System.getenv("GithubPermanentToken")}"))

        try {
            val statusCode = executeMethod(getJsonFileMethod)
            val responseBody = getJsonFileMethod.responseBodyAsStream.reader().readText()
            if (statusCode != HttpStatus.SC_OK) {
                getJsonFileMethod.releaseConnection()
                return null
            }
            val responseBodyJson = JsonParser.parseString(responseBody).asJsonObject
            getJsonFileMethod.releaseConnection()
            val contentJsonFile = responseBodyJson["content"].asString
            val decodedJsonFile = Base64.getMimeDecoder().decode(contentJsonFile).toString(Charsets.UTF_8)
            return JsonParser.parseString(decodedJsonFile).asJsonObject
        } catch (e: Exception) {
            println(e.message)
        } finally {
            getJsonFileMethod.releaseConnection()
        }
        return null
    }
}