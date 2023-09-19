package com.example.delivery

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import org.json.JSONObject
import java.util.HashMap

suspend fun sendHttpResponse(url: String, token: String, body: HashMap<*, *>): HttpResponse {
    val client = HttpClient{
        expectSuccess = false
        install(ContentNegotiation){json()}
    }
    val response: HttpResponse = client.get(url){
        contentType(ContentType.Application.Json)
        header("Authorization", "Token $token")
        setBody((body as Map<*, *>?)?.let { JSONObject(it).toString() })
    }
    return response
}