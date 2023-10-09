package com.example.delivery

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import org.json.JSONObject
import java.net.ConnectException
import java.util.HashMap

suspend fun sendHttpResponse(httpMethod: HttpMethod, url: String, token: String, body: HashMap<*, *>): HttpResponse {
    val client = HttpClient{
        expectSuccess = false
        install(ContentNegotiation){json()}
    }
    val response: HttpResponse = client.request(url){
        method = httpMethod
        contentType(ContentType.Application.Json)
        header("Authorization", "Token $token")
        setBody((body as Map<*, *>?)?.let { JSONObject(it).toString() })
    }
    return response
}

suspend fun login(category: String, username: String, password: String): LoginObject {
    val url = "http://192.168.1.9:8000/login"
    val map = HashMap<String, String>()
    map["category"] = category
    map["username"] = username
    map["password"] = password
    val loginObject: LoginObject = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = "", body = map)
        if (response.status.value in 200..299) response.body() else LoginObject(message = "Username or password not correct.")
    } catch (exception: ConnectException){
        LoginObject(message = "Connection Error.")
    }
    return loginObject
}

suspend fun whoAmI(token: String): Person{
    val url = "http://192.168.1.9:8000/who-am-i"
    val person: Person = try{
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else Person(message = "Unauthorized.")
    } catch (exception: ConnectException){
        Person(message = "Connection Error.")
    }
    return person
}

suspend fun getSellers(token: String): MutableList<Seller>{
    val url = "http://192.168.1.9:8000/sellers"
    val sellers: MutableList<Seller> = try{
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException){
        mutableListOf()
    }
    return sellers
}

suspend fun getSellerItems(token: String, sellerId: Int): MutableList<Item>{
    val url = "http://192.168.1.9:8000/sellers/$sellerId/items"
    val items: MutableList<Item> = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException){
        mutableListOf()
    }
    return items
}

suspend fun getItem(token: String, itemId: Int): Item{
    val url = "http://192.168.1.9:8000/items/$itemId"
    val item: Item = try{
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else Item()
    } catch (exception: ConnectException){
        Item()
    }
    return item
}

suspend fun getMyReview(token: String, itemId: Int): Review{
    val url = "http://192.168.1.9:8000/items/$itemId/my-review"
    val review: Review = try{
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else Review()
    } catch (exception: ConnectException){
        Review()
    }
    return review
}

suspend fun getItemReviews(token: String, itemId: Int): MutableList<Review>{
    val url = "http://192.168.1.9:8000/items/$itemId/reviews"
    val reviews: MutableList<Review> = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException){
        mutableListOf()
    }
    return reviews
}

suspend fun sendItemReview(token: String, itemId: Int, rating: Int, text: String): Review{
    val url = "http://192.168.1.9:8000/items/$itemId/send-review"
    val map = HashMap<String, Any>()
    map["text"] = text
    map["rating"] = rating
    val review: Review = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Post, url = url, token = token, body = map)
        if (response.status.value in 200..299) response.body() else Review()
    } catch (exception: ConnectException) {
        Review()
    }
    return review
}

suspend fun editItemReview(token: String, itemId: Int, rating: Int, text: String): Review{
    val url = "http://192.168.1.9:8000/items/$itemId/edit-review"
    val map = HashMap<String, Any>()
    map["text"] = text
    map["rating"] = rating
    val review: Review = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Put, url = url, token = token, body = map)
        if (response.status.value in 200..299) response.body() else Review()
    } catch (exception: ConnectException) {
        Review()
    }
    return review
}

suspend fun deleteItemReview(token: String, itemId: Int): Review{
    val url = "http://192.168.1.9:8000/items/$itemId/delete-review"
    val review: Review = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Delete, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else Review()
    } catch (exception: ConnectException) {
        Review()
    }
    return review
}

suspend fun getBasket(token: String): MutableList<BasketItem>{
    val url = "http://192.168.1.9:8000/basket"
    val basket: MutableList<BasketItem> = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Get, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException) {
        mutableListOf()
    }
    return basket
}

suspend fun addToBasket(token: String, itemId: Int, quantity: Int): BasketItem{
    val url = "http://192.168.1.9:8000/items/$itemId/add-to-basket"
    val map = HashMap<String, Any>()
    map["quantity"] = quantity
    val basketItem: BasketItem = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Post, url = url, token = token, body = map)
        if (response.status.value in 200..299) response.body() else BasketItem()
    } catch (exception: ConnectException) {
        BasketItem()
    }
    return basketItem
}

suspend fun editQuantity(token: String, itemId: Int, quantity: Int): BasketItem{
    val url = "http://192.168.1.9:8000/items/$itemId/edit-quantity"
    val map = HashMap<String, Any>()
    map["quantity"] = quantity
    val basketItem: BasketItem = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Patch, url = url, token = token, body = map)
        if (response.status.value in 200..299) response.body() else BasketItem()
    } catch (exception: ConnectException) {
        BasketItem()
    }
    return basketItem
}

suspend fun removeFromBasket(token: String, itemId: Int): BasketItem{
    val url = "http://192.168.1.9:8000/items/$itemId/remove-from-basket"
    val basketItem: BasketItem = try {
        val response = sendHttpResponse(httpMethod = HttpMethod.Delete, url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else BasketItem()
    } catch (exception: ConnectException) {
        BasketItem()
    }
    return basketItem
}