package com.example.delivery

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.delivery.ui.theme.DeliveryTheme
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import java.net.ConnectException

class ShopSellersActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        val token = intent.getStringExtra("Token")
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            var sellers by remember { mutableStateOf(mutableListOf<Seller>()) }

            LaunchedEffect(Unit){ coroutineScope.launch { coroutineScope.launch { sellers = getSellers(token!!) } }}
            DrawShopSellersLayout(sellers)
        }
    }
}

@kotlinx.serialization.Serializable
class Seller : Person(){
    val image: String = ""
}

suspend fun getSellers(token: String): MutableList<Seller>{
    val url = "http://192.168.1.9:8000/sellers"
    val sellers: MutableList<Seller> = try{
        val response = sendHttpResponse(url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException){
        mutableListOf()
    }
    return sellers
}

@Composable
fun DrawShopSellersLayout(sellers: MutableList<Seller>){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column() {
                AppBar(
                    title = "Shop",
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
                DrawShopSellersBackLayout(sellers = sellers)
            }
        }
    ) {
        it.calculateBottomPadding()
    }
}

@Composable
fun DrawShopSellersBackLayout(sellers: MutableList<Seller>){
    Column() {
        LazyColumn(Modifier){
            items(sellers){
                    seller -> SellerRow(base64String = seller.image, name = seller.firstName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowShopSellersPreview() {
    DeliveryTheme {
        //DrawShopSellersLayout()
    }
}