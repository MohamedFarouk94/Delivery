package com.example.delivery

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.Danger
import com.example.delivery.ui.theme.DeliveryTheme
import com.example.delivery.ui.theme.Wood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        val token = intent.getStringExtra("Token")
        val itemId = intent.getIntExtra("ItemId", 0)
        val itemName = intent.getStringExtra("ItemName")
        val myReviewId = intent.getIntExtra("MyReviewId", 0)
        val myReviewRating = intent.getIntExtra("MyReviewRating", 0)
        val myReviewText = intent.getStringExtra("MyReviewText")
        super.onCreate(savedInstanceState)

        setContent{
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val dismissFlag = remember { mutableStateOf(false) }
            val deleteFlag = remember { mutableStateOf(false) }
            DrawReviewLayout(
                context = context,
                coroutineScope = coroutineScope,
                token = token!!, itemId = itemId,
                itemName = itemName!!, editFlag = myReviewId > 0,
                enteredRating = remember { mutableStateOf(myReviewRating) },
                enteredReview = remember { mutableStateOf(myReviewText!!) },
                dismissFlag = dismissFlag,
                deleteFlag = deleteFlag)
            BackHandler {
                dismissFlag.value = true
            }
            if(dismissFlag.value) ShowAlertDialog(
                onDismissRequest = { dismissFlag.value = false},
                onConfirmation = { (context as Activity).finish() },
                dialogTitle = "Discard Changes",
                dialogText = "Are you sure? Changes will be lost."
            )
            if(deleteFlag.value) ShowAlertDialog(
                onDismissRequest = { deleteFlag.value = false },
                onConfirmation = {
                    coroutineScope.launch { deleteItemReview(token = token, itemId = itemId) }
                    Toast.makeText(context, "Review deleted successfully", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish() },
                dialogTitle = "Delete Review",
                dialogText = "Are you sure? Deleting cannot be undone."
            )
        }
    }
}

@Composable
fun DrawReviewLayout(context: Context,
                     coroutineScope: CoroutineScope,
                     token: String,
                     itemId: Int,
                     itemName: String,
                     editFlag: Boolean,
                     enteredRating: MutableState<Int>,
                     enteredReview: MutableState<String>,
                     dismissFlag: MutableState<Boolean>,
                     deleteFlag: MutableState<Boolean>){
    Column {
        // Up Comment
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.3f), Alignment.Center){
            Text(text = itemName, style = TextStyle(fontSize = 36.sp, textAlign = TextAlign.Center))
        }

        // Star-Rating Part
        val btnEnabledState = remember { mutableStateOf(enteredRating.value > 0) }
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f), Alignment.Center){
            RatingField(ratingState = enteredRating, isValidRating = btnEnabledState)
        }

        // Review Box Part
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(0.5f), Alignment.Center){
            TextField(value = enteredReview.value, onValueChange = { enteredReview.value = it},
                label = {Text (text = "Add review...")},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp))
        }

        // Buttons Part
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f)
            .padding(16.dp), Arrangement.End){
            TextButton(onClick = { dismissFlag.value = true }) {Text("Discard", style = TextStyle(color = Wood))}
            Spacer(Modifier.width(8.dp))
            if(editFlag) OneCaseButton(
                enabledText = "Delete",
                enabledBackgroundColor = Danger,
                enabledTextColor = Color.White,
                onClick = { deleteFlag.value = true }
            )
            if(editFlag) Spacer(Modifier.width(8.dp))
            TwoCasesButton(
                caseState = editFlag,
                enabledState = btnEnabledState,
                disabledText = "Send",
                falseCaseText = "Send",
                falseCaseBackgroundColor = Wood,
                falseCaseTextColor = Color.White,
                falseCaseClick = {
                    coroutineScope.launch { sendItemReview(token = token, itemId = itemId, rating = enteredRating.value, text = enteredReview.value) }
                    Toast.makeText(context, "Review sent successfully", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
                                 },
                trueCaseText = "Edit",
                trueCaseBackgroundColor = Wood,
                trueCaseTextColor = Color.White,
                trueCaseClick = {
                    coroutineScope.launch { editItemReview(token = token, itemId = itemId, rating = enteredRating.value, text = enteredReview.value) }
                    Toast.makeText(context, "Review updated successfully", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowReviewPreview() {
    DeliveryTheme {
        //DrawReviewLayout("Beefburger Sandwich", false, remember { mutableStateOf(0) }, remember { mutableStateOf("") }, remember { mutableStateOf(false) })
    }
}