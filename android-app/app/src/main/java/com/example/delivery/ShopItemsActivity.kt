package com.example.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

class ShopItemsActivity : ComponentActivity(){
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val sellerName = intent.getStringExtra("SellerName")
        val sellerId = intent.getIntExtra("SellerId", 0)
        super.onCreate(savedInstanceState)
        setContent{
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            var items by remember { mutableStateOf(mutableListOf<Item>()) }
            var basket by remember { mutableStateOf(mutableListOf<BasketItem>()) }
            val loadingKey = remember { mutableStateOf(false) }
            val chosenItem = remember { mutableStateOf(Item()) }
            var itemInBasket by remember { mutableStateOf(false) }
            val basketDialogFlag = remember { mutableStateOf(false) }
            val basketActionFlag = remember { mutableStateOf(false) }
            val basketAction = remember { mutableStateOf("") }
            val originalQuantity = remember { mutableStateOf(1) }
            val newQuantity = remember { mutableStateOf(1) }
            val showBasketDialog = remember { mutableStateOf(false) }
            val db = DataBaseHandler(context)
            val shortlistFlag = remember { mutableStateOf(false) }

            LaunchedEffect(loadingKey.value) {
                coroutineScope.launch {
                    items = getSellerItems(token!!, sellerId)

                    if(basketDialogFlag.value){
                        basket = getBasket(token)
                        itemInBasket = basket.any { basketItem -> basketItem.itemId ==  chosenItem.value.id}
                        originalQuantity.value = if(itemInBasket) basket.find { basketItem -> basketItem.itemId == chosenItem.value.id }?.quantity!! else 1
                        showBasketDialog.value = true
                        basketDialogFlag.value = false
                    }

                    if(basketActionFlag.value){
                        when (basketAction.value) {
                            "Add" -> { addToBasket(token, chosenItem.value.id, newQuantity.value); Toast.makeText(context,"Item is successfully added to your basket", Toast.LENGTH_SHORT).show() }
                            "Edit" -> { editQuantity(token, chosenItem.value.id, newQuantity.value); Toast.makeText(context,"Item quantity is successfully changed", Toast.LENGTH_SHORT).show() }
                            "Remove" -> { removeFromBasket(token, chosenItem.value.id); Toast.makeText(context,"Item is successfully removed from your basket", Toast.LENGTH_SHORT).show() }
                        }
                        basketActionFlag.value = false
                    }

                    if(shortlistFlag.value){
                        if(db.contains(chosenItem.value.id))
                            Toast.makeText(context, "Item is already shortlisted!", Toast.LENGTH_SHORT).show()
                        else{
                            db.addItem(chosenItem.value)
                            Toast.makeText(context, "Item is successfully shortlisted!", Toast.LENGTH_SHORT).show()
                        }
                       shortlistFlag.value = false
                    }
                }
            }

            if(showBasketDialog.value) BasketDialog(
                itemName = chosenItem.value.name,
                originalQuantity = originalQuantity.value,
                newQuantity = newQuantity,
                unitPrice = chosenItem.value.price,
                edit = itemInBasket,
                setShow = { showBasketDialog.value = it },
                addToBasket = { basketAction.value = "Add"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value },
                editQuantity = { basketAction.value = "Edit"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value },
                removeFromBasket = { basketAction.value = "Remove"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value })

            DrawShopItemsLayout(token = token!!,
                                sellerName = sellerName!!,
                                items = items,
                                chosenItem = chosenItem,
                                basketDialogFlag = basketDialogFlag,
                                shortlistFlag = shortlistFlag,
                                loadingKey = loadingKey)
        }
    }
}

@Composable
fun DrawShopItemsLayout(token: String,
                        sellerName: String,
                        items: MutableList<Item>,
                        chosenItem: MutableState<Item>,
                        basketDialogFlag: MutableState<Boolean>,
                        shortlistFlag: MutableState<Boolean>,
                        loadingKey: MutableState<Boolean>){
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
        padding -> DrawShopItemsBackLayout(padding = padding,
                                           token = token,
                                           items = items,
                                           chosenItem = chosenItem,
                                           basketDialogFlag = basketDialogFlag,
                                           shortlistFlag = shortlistFlag,
                                           loadingKey = loadingKey)
    }
}

@Composable
fun DrawShopItemsBackLayout(padding: PaddingValues,
                            token: String,
                            items: MutableList<Item>,
                            chosenItem: MutableState<Item>,
                            basketDialogFlag: MutableState<Boolean>,
                            shortlistFlag: MutableState<Boolean>,
                            loadingKey: MutableState<Boolean>){
    val context = LocalContext.current
    Column(modifier = Modifier.padding(padding)) {
        if(items.size > 0) LazyColumn(Modifier, userScrollEnabled = true){
            items(items){
                    item -> ItemRow(item = item,
                                    onBasketClick = { chosenItem.value = item; basketDialogFlag.value = true; loadingKey.value = !loadingKey.value },
                                    onShortlistClick = { chosenItem.value = item; shortlistFlag.value = true; loadingKey.value = !loadingKey.value }){
                        context.startActivity(Intent(context, ItemActivity::class.java).also {
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