package com.example.delivery

import android.net.http.HttpResponseCache.install
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Absolute.aligned
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpHeaders
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.cio.parseResponse
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.Identity.decode
import io.ktor.utils.io.ByteReadChannel
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

@kotlinx.serialization.Serializable
class LoginObject(val token: String)

suspend fun sendHttpRequest(category: String, username: String, password: String): io.ktor.client.statement.HttpResponse {
    val map = HashMap<String, String>()
    map["category"] = category
    map["username"] = username
    map["password"] = password
    val url = "http://192.168.1.9:8000/login"
    val client = HttpClient(){
        expectSuccess = false
        install(ContentNegotiation){json()}
    }
    val response: io.ktor.client.statement.HttpResponse = client.get(url){
        contentType(ContentType.Application.Json)
        setBody(JSONObject(map as Map<*, *>?).toString())
    }
    return response
}

@Composable
fun drawLayout() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var userType by remember { mutableStateOf( "Customer") }
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

        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(userType)
            Box(modifier = Modifier) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "More"
                    )
                }
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Customer") },
                        onClick = { userType = "Customer" })
                    DropdownMenuItem(
                        text = { Text("Pilot") },
                        onClick = { userType = "Pilot" })
                }
            }
        }
        
        Button(onClick = { coroutineScope.launch {
                val response = sendHttpRequest("Seller", username, password)
                Log.d("login", response.status.toString())
                if(response.status.value in 200..299) {
                    val loginObject: LoginObject = response.body()
                    Log.d("login", "token: ${loginObject.token}")
                }
                else
                    Log.d("login", response.body())

            }
        }) {Text("Login")}

        Text("Don't have an account? Sign up.")
    }
}

@Preview(showBackground = true)
@Composable
fun showPreview() {
    DeliveryTheme {
        drawLayout()
    }
}