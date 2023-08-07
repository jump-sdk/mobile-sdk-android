package com.spreedly.client

import com.spreedly.client.models.putAsJsonElement
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

private const val BASE_URL = "https://core.spreedly.com"
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
internal expect val engine: HttpClientEngine

class KtorClient(
    private val key: String,
    private val secret: String?,
) {
    private val credentials: String by lazy { "Basic " + safeBase64("$key:$secret".toByteArray()) }

    private val client = HttpClient(engine) {
        expectSuccess = false
        install(ContentNegotiation) {
            @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
            json(
                Json {
                    encodeDefaults = true
                    isLenient = true
                    prettyPrint = false
                    ignoreUnknownKeys = true
                    explicitNulls = false
                },
            )
        }
        defaultRequest {
            url {
                takeFrom(BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }

    internal suspend fun sendRequest(
        requestBody: JsonObject,
        url: String,
        authenticated: Boolean,
    ): JsonObject = withContext(ioDispatcher) {
        val authenticatedBody = if (!authenticated) {
            val jsonEntries: MutableMap<String, JsonElement> = requestBody.toMutableMap()
            jsonEntries.putAsJsonElement("environment_key", key)
            JsonObject(jsonEntries)
        } else {
            requestBody
        }

        client
            .post(url) {
                setBody(authenticatedBody)
                if (authenticated) {
                    header("Authorization", credentials)
                }
            }
            .body()
    }
}

@OptIn(ExperimentalEncodingApi::class)
internal fun safeBase64(source: ByteArray): String = Base64.encode(source)
    .replace("\n", "")
    .replace("\r", "")
