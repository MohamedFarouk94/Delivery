package com.example.delivery

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.DeliveryTheme
import kotlinx.coroutines.launch
import java.util.Comparator

class ItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val itemId = intent.getIntExtra("ItemId", 0)
        super.onCreate(savedInstanceState)

        setContent{
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
                    if(reviews.size > 0) reviews = reviews.sortedWith(compareBy({-it.isMine.toInt()}, {-it.isText.toInt()}, {it.id})) as MutableList<Review>
                }
            }
            DrawItemLayout(item = item, reviews = reviews)
        }
    }
}

@Composable
fun DrawItemLayout(item: Item, reviews: MutableList<Review>){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Column() {
                AppBar(
                    title = item.name,
                    icon = Icons.Default.ArrowBack,
                    onIconClick = { (context as Activity).finish() },
                    contentDescription = "Back"
                )
                DrawItemBackLayout(item = item, reviews = reviews)
            }
        }
    ) {
        it.calculateBottomPadding()
    }
}

@Composable
fun DrawItemBackLayout(item: Item, reviews: MutableList<Review>){
    val b64 = item.image.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    val description = if (item.description in listOf("", "null")) "No description available" else item.description
    val price = "${item.price} EGP"
    val rating = if (item.rating == null) "Not Available" else "${item.rating.format(2)}"

    // Item Part
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        if(decodedImage != null) Image(bitmap = decodedImage.asImageBitmap(), contentDescription = item.name,
            modifier = Modifier
                .weight(0.3f)
                .padding(1.dp))
        Column(modifier = Modifier.weight(0.7f)) {
            Text(text = item.name, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 36.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description)
            Text(text = "Price: $price", style = TextStyle(fontWeight = FontWeight.Bold))
            Row(){ Text(modifier = Modifier.padding(vertical = 2.dp), text = "Rating: $rating", style = TextStyle(fontWeight = FontWeight.Bold)); Icon(imageVector = Icons.Default.Star, contentDescription = "star", tint = Color(0xffffd700)) }
        }
    }

    // Buttons Part
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(16.dp))
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Button(onClick = { /*TODO*/ }) { Text(text = "Add to Basket") }
        Spacer(modifier = Modifier.width(36.dp))
        Button(onClick = { /*TODO*/ }) { Text(text = "Add to Shortlist") }
    }
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Button(onClick = { /*TODO*/ }) { Text(text = "Rate / Review") }
    }
    // Reviews Part
    Column() {
        LazyColumn(userScrollEnabled = true) {
            items(reviews) { review ->
                ReviewRow(review = review)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowItemPreview() {
    DeliveryTheme {
        val description = "KFC's special rice with chicken breast fillet and spicy sauce or barbeque sauceSPICY SAUCEBBQ SAUCE"
        val b64 = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\\nHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\\nMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADfASEDASIA\\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iii\\ngAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKA\\nCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAK\\nKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoo\\nooASio5JkiHzkc9qz59Xjh8xwGkVOCFHT/Gsp1oQdmyowctjUo/Cs86pCFYqDuUZK9xXOeKPFl5p\\nNmtxZQRsobEhk59Rxj6VDxVJdTSnh6k5cqR2WaK4a0+KOgSaYlzdSvBPj5oChYg+2BzWr4Z8Z6X4\\n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\\n"
        val item = Item(id=15, name = "Rizo", sellerId = 2, sellerUsername = "kfc",
            description = description, price = 65.99, image = b64, rating = 4.67899999, numberOfRaters = 3)
        DrawItemLayout(item, mutableListOf())
    }
}