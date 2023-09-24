package com.example.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.ktor.client.call.body
import kotlinx.coroutines.launch
import java.net.ConnectException

class ShopItemsActivity : ComponentActivity(){
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val sellerName = intent.getStringExtra("SellerName")
        val sellerId = intent.getIntExtra("SellerId", 0)
        super.onCreate(savedInstanceState)
        setContent{
            val coroutineScope = rememberCoroutineScope()
            var items by remember { mutableStateOf(mutableListOf<Item>()) }

            LaunchedEffect(Unit) { coroutineScope.launch { items = getSellerItems(token!!, sellerId) } }
            DrawShopItemsLayout(token = token!!, sellerName = sellerName!!, items = items)
        }
    }
}

@Composable
fun DrawShopItemsLayout(token: String, sellerName: String, items: MutableList<Item>){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column {
                AppBar(
                    title = sellerName,
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
            }
        }
    ) {
        padding -> DrawShopItemsBackLayout(padding = padding, token = token, items = items)
    }
}

@Composable
fun DrawShopItemsBackLayout(padding: PaddingValues, token: String, items: MutableList<Item>){
    val context = LocalContext.current
    Column(modifier = Modifier.padding(padding)) {
        if(items.size > 0) LazyColumn(Modifier, userScrollEnabled = true){
            items(items){
                    item -> ItemRow(item){context.startActivity(Intent(context, ItemActivity::class.java).also {
                        it.putExtra("Token", token)
                        it.putExtra("ItemId", item.id)
            })}
            }
        }
        else Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) { Text(text = "No items found.") }
    }
}