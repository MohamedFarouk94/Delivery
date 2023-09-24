package com.example.delivery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.launch

class ItemActivity : ComponentActivity() {
    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val itemId = intent.getIntExtra("ItemId", 0)
        super.onCreate(savedInstanceState)

        setContent{
            val refreshScope = rememberCoroutineScope()
            val coroutineScope = rememberCoroutineScope()
            var item by remember { mutableStateOf(Item()) }
            var reviews by remember { mutableStateOf(mutableListOf<Review>()) }
            var myReview by remember { mutableStateOf(Review()) }

            LaunchedEffect(Unit){
                coroutineScope.launch {
                    item = getItem(token!!, itemId)
                    reviews = getItemReviews(token, itemId)
                    myReview = getMyReview(token, itemId)
                    if(reviews.size > 0) reviews = reviews.map { review ->  review.checkIfMine(myReview.id).checkIfText()} as MutableList<Review>
                    if(reviews.size > 0) reviews = reviews.sortedWith(compareBy({-it.isMine.toInt()}, {-it.isText.toInt()}, {-it.id})) as MutableList<Review>
                }
            }
            DrawItemLayout(token = token!!, item = item, myReview = myReview, reviews = reviews)
        }
    }
}

@Composable
fun DrawItemLayout(token: String, item: Item, myReview: Review, reviews: MutableList<Review>){
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
        padding -> DrawItemBackLayout(padding = padding, token = token, item = item, myReview = myReview, reviews = reviews)
    }
}

@Composable
fun DrawItemBackLayout(padding: PaddingValues, token: String, item: Item, myReview: Review, reviews: MutableList<Review>){
    val context = LocalContext.current

    val b64 = item.image.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    val description = if (item.description in listOf("", "null")) "No description available" else item.description
    val price = "${item.price} EGP"
    val rating = if (item.rating == null) "Not Available" else "${item.rating.format(2)}"

    Column (modifier = Modifier.padding(padding)){
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
            Button(onClick = { /*TODO*/ }) { Text(text = "Add to Basket") }
            Spacer(modifier = Modifier.width(36.dp))
            Button(onClick = { /*TODO*/ }) { Text(text = "Add to Shortlist") }
        }
        val onClick = {
            context.startActivity(Intent(context, ReviewActivity::class.java).also {
                it.putExtra("Token", token)
                it.putExtra("ItemId", item.id)
                it.putExtra("ItemName", item.name)
                it.putExtra("MyReviewId", myReview.id)
                it.putExtra("MyReviewRating", myReview.rating)
                it.putExtra("MyReviewText", myReview.text)
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TwoCasesButton(
                caseState = myReview.id > 0,
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
        Column{
            if(reviews.size > 0) LazyColumn(userScrollEnabled = true) {
                items(reviews) { review ->
                    ReviewRow(review = review)
                }
            }
            else Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) { Text(text = "No reviews found.") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowItemPreview() {
    DeliveryTheme {

    }
}