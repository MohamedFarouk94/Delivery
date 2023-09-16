package com.example.delivery

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.delivery.ui.theme.DeliveryTheme
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable
import java.util.HashMap

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            drawLayout()
        }
    }
}

suspend fun sendHttpRequest(category: String, username: String, password: String): String{
    Log.d("login", "inside sendHttpRequest before response")
    val map = HashMap<String, String>()
    map["category"] = category
    map["username"] = username
    map["password"] = password
    val url = "http://192.168.1.9:8000/login"
    val client = HttpClient()
    val response = client.get<String>(url){
        contentType(ContentType.Application.Json)
        body = JSONObject(map as Map<*, *>?).toString()
    }
    Log.d("login", "inside sendHttpRequest inside response")
    return response
}

@Composable
fun drawLayout() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
        TextField(value = username,
            onValueChange = {username = it},
            label = {Text("Username")},
            singleLine = true)

        TextField(value = password,
            onValueChange = {password = it},
            label = {Text("Password")},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )
        
        Button(onClick = { coroutineScope.launch {
            Log.d("login","inside onClick before response")
            val response = sendHttpRequest("Seller", username, password)
            Log.d("login", response)} }) {Text("Login")}
    }
}

@Preview(showBackground = true)
@Composable
fun showPreview() {
    DeliveryTheme {
        drawLayout()
    }
}