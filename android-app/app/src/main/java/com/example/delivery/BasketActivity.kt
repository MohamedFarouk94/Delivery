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

class BasketActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        super.onCreate(savedInstanceState)

        setContent{
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val loadingKey = remember { mutableStateOf(false) }
            var basket by remember { mutableStateOf(mutableListOf<BasketItem>()) }
            var items by remember { mutableStateOf(mutableListOf<Item>()) }
            val chosenItem = remember{ mutableStateOf(Item()) }
            val showBasketDialog = remember { mutableStateOf(false) }
            val basketDialogFlag = remember { mutableStateOf(false) }
            val basketActionFlag = remember { mutableStateOf(false) }
            val basketAction = remember { mutableStateOf("") }
            val originalQuantity = remember { mutableStateOf(0) }
            val newQuantity = remember { mutableStateOf(0) }
            val db = DataBaseHandler(context)
            val shortlistFlag = remember { mutableStateOf(false) }
            val showShortlistDialog = remember { mutableStateOf(false) }

            LaunchedEffect(loadingKey.value){
                coroutineScope.launch {
                    basket = getBasket(token!!)
                    if(basket.size > 0) items = basket.map { basketItem -> getItem(token, basketItem.itemId) } as MutableList<Item>

                    if(basketDialogFlag.value){
                        originalQuantity.value = basket.find { basketItem -> basketItem.itemId == chosenItem.value.id }?.quantity!!
                        showBasketDialog.value = true
                        basketDialogFlag.value = false
                    }

                    if(basketActionFlag.value){
                        when (basketAction.value) {
                            // No Add
                            "Edit" -> { editQuantity(token, chosenItem.value.id, newQuantity.value); Toast.makeText(context,"Item quantity is successfully changed", Toast.LENGTH_SHORT).show() }
                            "Remove" -> { removeFromBasket(token, chosenItem.value.id); Toast.makeText(context,"Item is successfully removed from your basket", Toast.LENGTH_SHORT).show() }
                        }
                        basketActionFlag.value = false
                        loadingKey.value = !loadingKey.value // Refresh again
                    }

                    if(shortlistFlag.value){
                        if(db.contains(chosenItem.value.id))
                            showShortlistDialog.value = true

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
                edit = true,    // Because all items here are already in the basket
                setShow = { showBasketDialog.value = it },
                addToBasket = { /** Unused **/ },   // Because all items here are already in the basket
                editQuantity = { basketAction.value = "Edit"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value },
                removeFromBasket = { basketAction.value = "Remove"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value })

            if(showShortlistDialog.value) ShowAlertDialog(
                onDismissRequest = { showShortlistDialog.value = false },
                onConfirmation = { db.removeItem(chosenItem.value.id); showShortlistDialog.value = false; Toast.makeText(context, "Item is removed from shortlist!", Toast.LENGTH_SHORT).show() },
                dialogTitle = "Remove From Shortlist",
                dialogText = "This item is already shortlisted. Do you want to remove it from shortlist?"
            )

            DrawBasketLayout(token = token!!,
                            basket = basket,
                            items = items,
                            chosenItem = chosenItem,
                            basketDialogFlag = basketDialogFlag,
                            shortlistFlag = shortlistFlag,
                            loadingKey = loadingKey)
        }
    }
}

@Composable
fun DrawBasketLayout(token: String,
                     basket: MutableList<BasketItem>,
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
                    title = "Basket",
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
            }
        }
    ) {
        padding -> DrawBasketBackLayout(padding = padding,
                                        token = token,
                                        basket = basket,
                                        items = items,
                                        chosenItem = chosenItem,
                                        basketDialogFlag = basketDialogFlag,
                                        shortlistFlag = shortlistFlag,
                                        loadingKey = loadingKey)
    }
}

@Composable
fun DrawBasketBackLayout(padding: PaddingValues,
                         token: String,
                         basket: MutableList<BasketItem>,
                         items: MutableList<Item>,
                         chosenItem: MutableState<Item>,
                         basketDialogFlag: MutableState<Boolean>,
                         shortlistFlag: MutableState<Boolean>,
                         loadingKey: MutableState<Boolean>) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(padding)) {
        if(items.size > 0) LazyColumn(Modifier, userScrollEnabled = true){
            items(items.zip(basket)){
                pair -> ItemRow(item = pair.component1(),
                                quantity = pair.component2().quantity,
                                onBasketClick = { chosenItem.value = pair.component1(); basketDialogFlag.value = true; loadingKey.value = !loadingKey.value },
                                onShortlistClick = { chosenItem.value = pair.component1(); shortlistFlag.value = true; loadingKey.value = !loadingKey.value }) {
                context.startActivity(Intent(context, ItemActivity::class.java).also {
                    it.putExtra("Token", token)
                    it.putExtra("ItemId", pair.component1().id)
                })}
            }
        }
        else Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) { Text(text = "No items found.") }
    }
}