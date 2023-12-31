package com.example.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.delivery.ui.theme.DeliveryTheme
import kotlinx.coroutines.launch

class ShopSellersActivity : ComponentActivity(){
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        val token = intent.getStringExtra("Token")
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            var sellers by remember { mutableStateOf(mutableListOf<Seller>()) }

            LaunchedEffect(Unit){ coroutineScope.launch { coroutineScope.launch { sellers = getSellers(token!!) } }}
            DrawShopSellersLayout(token!!, sellers)
        }
    }
}

@Composable
fun DrawShopSellersLayout(token: String, sellers: MutableList<Seller>){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column{
                AppBar(
                    title = "Shop",
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
            }
        }
    ) {
        padding -> DrawShopSellersBackLayout(padding = padding, token = token, sellers = sellers)
    }
}

@Composable
fun DrawShopSellersBackLayout(padding: PaddingValues, token: String, sellers: MutableList<Seller>){
    val context = LocalContext.current
    Column(modifier = Modifier.padding(padding)) {
        LazyColumn(Modifier, userScrollEnabled = true){
            items(sellers){
                    seller -> SellerRow(base64String = seller.image, name = seller.firstName){
                        context.startActivity(Intent(context, ShopItemsActivity::class.java)
                            .also { it.putExtra("Token", token)
                                    it.putExtra("SellerName", seller.firstName)
                                    it.putExtra("SellerId", seller.id)})
            }
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