package com.example.delivery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.delivery.ui.theme.DeliveryTheme
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import java.util.HashMap
import java.net.ConnectException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrawLoginLayout()
        }
    }
}

@kotlinx.serialization.Serializable
class LoginObject(val token: String = "", val message: String = "ok")

suspend fun login(category: String, username: String, password: String): LoginObject {
    val url = "http://192.168.1.9:8000/login"
    val map = HashMap<String, String>()
    map["category"] = category
    map["username"] = username
    map["password"] = password
    val loginObject: LoginObject = try {
        val response = sendHttpResponse(url = url, token = "", body = map)
        if (response.status.value in 200..299) response.body() else LoginObject(message = "Username or password not correct.")
    } catch (exception: ConnectException){
        LoginObject(message = "Connection Error.")
    }
    return loginObject
}

@Composable
fun DrawLoginLayout() {
    val usernameState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val userTypeState = remember { mutableStateOf("Customer") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

        OneLineField(value = "Username", mutableState = usernameState)
        PasswordField(value = "Password", passwordState = passwordState)
        OptionsMenu(userTypeState = userTypeState)
        
        Button(onClick = { coroutineScope.launch {
            val loginObject = login(category = userTypeState.value, username = usernameState.value, password = passwordState.value)
            // val loginObject = LoginObject(token="TemporaryToken")
            if(loginObject.message == "ok"){
                context.startActivity(Intent(context, HomeActivity::class.java).also {
                    it.putExtra("Token", loginObject.token)
                    it.putExtra("Category", userTypeState.value) })
                (context as Activity).finish()
            }
            else {
                Toast.makeText(context, loginObject.message, Toast.LENGTH_SHORT).show()
            }
        }
        }) {Text("Login")}

        Text("Don't have an account? Sign up.")
    }
}

@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    DeliveryTheme {
        DrawLoginLayout()
    }
}