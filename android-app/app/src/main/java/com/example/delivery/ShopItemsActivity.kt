package com.example.delivery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import java.net.ConnectException

class ShopItemsActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val sellerName = intent.getStringExtra("SellerName")
        val sellerId = intent.getIntExtra("SellerId", 0)
        super.onCreate(savedInstanceState)
        setContent{
            val coroutineScope = rememberCoroutineScope()
            var items by remember { mutableStateOf(mutableListOf<Item>()) }

            LaunchedEffect(Unit){ coroutineScope.launch { coroutineScope.launch { items = getSellerItems(token!!, sellerId) } }}
            DrawShopItemsLayout(token = token!!, sellerName = sellerName!!, items = items)

        }
    }
}

@kotlinx.serialization.Serializable
class Item(val id: Int = 0,
           val name: String = "",
           val sellerId: Int = 0,
           val sellerUsername: String = "",
           val description: String = "",
           val price: Double = 0.0,
           val image: String = "",
           val rating: Double? = null,
           val numberOfRaters: Int = 0,
           val numberOfOrders: Int = 0,
           val numberOfBuyouts: Int = 0)


suspend fun getSellerItems(token: String, sellerId: Int): MutableList<Item>{
    val url = "http://192.168.1.9:8000/sellers/$sellerId/items"
    val items: MutableList<Item> = try {
        val response = sendHttpResponse(url = url, token = token, body = HashMap<String, String>())
        if (response.status.value in 200..299) response.body() else mutableListOf()
    } catch (exception: ConnectException){
        mutableListOf()
    }
    return items
}

@Composable
fun DrawShopItemsLayout(token: String, sellerName: String, items: MutableList<Item>){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column() {
                AppBar(
                    title = sellerName,
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
                DrawShopItemsBackLayout(token, items)
            }
        }
    ) {
        it.calculateBottomPadding()
    }
}

@Composable
fun DrawShopItemsBackLayout(token: String, items: MutableList<Item>){
    val context = LocalContext.current
    Column() {
        LazyColumn(Modifier, userScrollEnabled = true){
            items(items){
                    item -> ItemRow(item){}
            }
        }
    }
}