package com.example.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.DeliveryTheme
import com.example.delivery.ui.theme.Wood
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ItemActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val itemId = intent.getIntExtra("ItemId", 0)
        super.onCreate(savedInstanceState)

        setContent{
            val context = LocalContext.current
            val refreshScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }
            val loadingKey = remember { mutableStateOf(false) }
            val openReviewActivity = remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            var item by remember { mutableStateOf(Item()) }
            var reviews by remember { mutableStateOf(mutableListOf<Review>()) }
            val myReviewState = remember { mutableStateOf(Review()) }
            val showBasketDialog = remember { mutableStateOf(false) }
            var basket by remember { mutableStateOf(mutableListOf<BasketItem>()) }
            var itemInBasket by remember { mutableStateOf(basket.any { basketItem ->  basketItem.itemId == itemId}) }
            val basketActionFlag = remember { mutableStateOf(false) }
            val basketAction = remember { mutableStateOf("") }
            val originalQuantity = remember { mutableStateOf(1) }
            val newQuantity = remember { mutableStateOf(1) }

            fun refresh() = refreshScope.launch{
                refreshing = true
                delay(1000)
                refreshing = false
                loadingKey.value = !loadingKey.value
            }
            val refreshState = rememberPullRefreshState(refreshing, ::refresh)

            LaunchedEffect(loadingKey.value){
                coroutineScope.launch {
                    item = getItem(token!!, itemId)
                    reviews = getItemReviews(token, itemId)
                    myReviewState.value = getMyReview(token, itemId)
                    if(reviews.size > 0) reviews = reviews.map { review ->  review.checkIfMine(myReviewState.value.id).checkIfText()} as MutableList<Review>
                    if(reviews.size > 0) reviews = reviews.sortedWith(compareBy({-it.isMine.toInt()}, {-it.isText.toInt()}, {-it.id})) as MutableList<Review>
                    basket = getBasket(token)
                    itemInBasket = basket.any { basketItem -> basketItem.itemId == itemId }
                    originalQuantity.value = if(itemInBasket) basket.find { basketItem -> basketItem.itemId == itemId }?.quantity!! else 1

                    if(openReviewActivity.value) {
                        context.startActivity(Intent(context, ReviewActivity::class.java).also {
                            it.putExtra("Token", token)
                            it.putExtra("ItemId", item.id)
                            it.putExtra("ItemName", item.name)
                            it.putExtra("MyReviewId", myReviewState.value.id)
                            it.putExtra("MyReviewRating", myReviewState.value.rating)
                            it.putExtra("MyReviewText", myReviewState.value.text)
                        })
                        openReviewActivity.value = false
                    }

                    if(basketActionFlag.value){
                        when (basketAction.value) {
                            "Add" -> { addToBasket(token, itemId, newQuantity.value); Toast.makeText(context,"Item is successfully added to your basket", Toast.LENGTH_SHORT).show() }
                            "Edit" -> { editQuantity(token, itemId, newQuantity.value); Toast.makeText(context,"Item quantity is successfully changed", Toast.LENGTH_SHORT).show() }
                            "Remove" -> { removeFromBasket(token, itemId); Toast.makeText(context,"Item is successfully removed from your basket", Toast.LENGTH_SHORT).show() }
                        }
                        basketActionFlag.value = false
                        loadingKey.value = !loadingKey.value // loading again
                    }
                }
            }

            if(showBasketDialog.value) BasketDialog(
                itemName = item.name,
                originalQuantity = originalQuantity.value,
                newQuantity = newQuantity,
                unitPrice = item.price,
                edit = itemInBasket,
                setShow = { showBasketDialog.value = it },
                addToBasket = { basketAction.value = "Add"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value },
                editQuantity = { basketAction.value = "Edit"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value },
                removeFromBasket = { basketAction.value = "Remove"; basketActionFlag.value = true; loadingKey.value = !loadingKey.value })

            DrawItemLayout(refreshState = refreshState,
                           refreshing = refreshing,
                           loadingKey = loadingKey,
                           openReviewActivity = openReviewActivity,
                           item = item,
                           myReviewState = myReviewState,
                           reviews = reviews,
                           showBasketDialog = showBasketDialog,
                           itemInBasket = itemInBasket)
        }
    }
}

@Composable
fun DrawItemLayout(refreshState: PullRefreshState,
                   refreshing: Boolean,
                   loadingKey: MutableState<Boolean>,
                   openReviewActivity: MutableState<Boolean>,
                   item: Item,
                   myReviewState: MutableState<Review>,
                   reviews: MutableList<Review>,
                   showBasketDialog: MutableState<Boolean>,
                   itemInBasket: Boolean){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column {
                AppBar(
                    title = item.name,
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
            }
        }
    ) {
        padding -> DrawItemBackLayout(refreshState = refreshState,
                                      refreshing = refreshing,
                                      loadingKey = loadingKey,
                                      openReviewActivity = openReviewActivity,
                                      padding = padding,
                                      item = item,
                                      myReviewState = myReviewState,
                                      reviews = reviews,
                                      showBasketDialog = showBasketDialog,
                                      itemInBasket = itemInBasket)
    }
}

@Composable
fun DrawItemBackLayout(refreshState: PullRefreshState,
                       refreshing: Boolean,
                       loadingKey: MutableState<Boolean>,
                       openReviewActivity: MutableState<Boolean>,
                       padding: PaddingValues,
                       item: Item,
                       myReviewState: MutableState<Review>,
                       reviews: MutableList<Review>,
                       showBasketDialog: MutableState<Boolean>,
                       itemInBasket: Boolean){

    val b64 = item.image.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    val description = if (item.description in listOf("", "null", "nan", null)) "No description available" else item.description
    val price = "${item.price} EGP"
    val rating = if (item.rating == null) "Not Available" else "${item.rating.format(2)}"

    Column(modifier = Modifier
        .padding(padding)
        .pullRefresh(state = refreshState)) {

        // Item Part
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (decodedImage != null) Image(
                bitmap = decodedImage.asImageBitmap(), contentDescription = item.name,
                modifier = Modifier
                    .weight(0.3f)
                    .padding(1.dp)
            )
            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = item.name,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description)
                Text(text = "Price: $price", style = TextStyle(fontWeight = FontWeight.Bold))
                Row {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp),
                        text = "Rating: $rating",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    ); Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star",
                    tint = Color(0xffffd700)
                )
                }
            }
        }

        // Buttons Part
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TwoCasesButton(
                caseState = itemInBasket,
                falseCaseText = "Add to Basket",
                falseCaseBackgroundColor = Wood,
                falseCaseTextColor = Color.White,
                falseCaseClick = { showBasketDialog.value = true },
                trueCaseText = "Edit Basket",
                trueCaseBackgroundColor = Wood,
                trueCaseTextColor = Color.White,
                trueCaseClick = { showBasketDialog.value = true }
            )
            Spacer(modifier = Modifier.width(36.dp))
            TwoCasesButton(
                caseState = false,
                falseCaseText = "Add to Shortlist",
                falseCaseBackgroundColor = Wood,
                falseCaseTextColor = Color.White,
                falseCaseClick = {},
                trueCaseText = "Remove from Shortlist",
                trueCaseBackgroundColor = Wood,
                trueCaseTextColor = Color.White,
                trueCaseClick = {}
            )
        }
        val onClick = {
                openReviewActivity.value = true
                loadingKey.value = !loadingKey.value
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TwoCasesButton(
                caseState = myReviewState.value.id > 0,
                falseCaseText = "Rate / Review",
                falseCaseBackgroundColor = Wood,
                falseCaseTextColor = Color.White,
                falseCaseClick = onClick,
                trueCaseText = "Edit / Delete Review",
                trueCaseBackgroundColor = Wood,
                trueCaseTextColor = Color.White,
                trueCaseClick = onClick
            )
        }

        // Reviews Part
        Column {
            // Refresh
            Spacer(modifier = Modifier.height(28.dp))
            PullRefreshIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally),
                refreshing = refreshing,
                state = refreshState
            )
            if (reviews.size > 0) {
                LazyColumn(userScrollEnabled = true) {
                    items(reviews) { review ->
                        ReviewRow(review = review)
                    }
                }
            } else Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { Text(text = "No reviews found.") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowItemPreview() {
    DeliveryTheme {

    }
}