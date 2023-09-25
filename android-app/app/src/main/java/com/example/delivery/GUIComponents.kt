package com.example.delivery

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.style.BackgroundColorSpan
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.DeliveryTheme
import com.example.delivery.ui.theme.Tortilla
import com.example.delivery.ui.theme.Wood
import java.nio.file.WatchEvent

data class MenuItem(val title: String, val icon: ImageVector, val onClick: ()->Unit)

@Composable
fun TwoCasesButton(caseState: Boolean = false,
                   enabledState: MutableState<Boolean> = remember { mutableStateOf(true) },
                   disabledText: String = "", disabledBackgroundColor: Color = Tortilla, disabledTextColor: Color = Wood,
                   falseCaseText: String, falseCaseBackgroundColor: Color, falseCaseTextColor: Color, falseCaseClick: ()->Unit,
                   trueCaseText: String, trueCaseBackgroundColor: Color, trueCaseTextColor: Color, trueCaseClick: ()->Unit){

    Log.d("item", enabledState.value.toString())
    val onClick = if(caseState) trueCaseClick else falseCaseClick
    val backgroundColor = if(!enabledState.value) disabledBackgroundColor else if(caseState) trueCaseBackgroundColor else falseCaseBackgroundColor
    val textColor = if(!enabledState.value) disabledTextColor else if(caseState) trueCaseTextColor else falseCaseTextColor
    val text = if(!enabledState.value) disabledText else if(caseState) trueCaseText else falseCaseText
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(backgroundColor), enabled = enabledState.value) {
        Text(text = text, style = TextStyle(color = textColor))
    }
}

@Composable
fun OneCaseButton(enabledState: MutableState<Boolean> = remember { mutableStateOf(true) },
                  disabledText: String = "", disabledBackgroundColor: Color = Tortilla, disabledTextColor: Color = Wood,
                  enabledText: String, enabledBackgroundColor: Color, enabledTextColor: Color, onClick: ()->Unit){
    TwoCasesButton(
        enabledState = enabledState,
        disabledText = disabledText,
        disabledBackgroundColor = disabledBackgroundColor,
        disabledTextColor = disabledTextColor,
        falseCaseText = enabledText,
        falseCaseBackgroundColor = enabledBackgroundColor,
        falseCaseTextColor = enabledTextColor,
        falseCaseClick = onClick,
        trueCaseText = "", // Unused
        trueCaseBackgroundColor = Wood, // Unused
        trueCaseTextColor = Wood, // Unused
        trueCaseClick = {} // Unused
    )
}

@Composable
fun OneLineField(value: String, mutableState: MutableState<String>){
    TextField(value = mutableState.value,
        onValueChange = {mutableState.value = it},
        label = { Text(value) },
        singleLine = true)
}

@Composable
fun PasswordField(value: String, passwordState: MutableState<String>){
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(value) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(imageVector  = image, description)
            }
        }
    )
}

@Composable
fun OptionsMenu(userTypeState: MutableState<String>){
    var expanded by remember { mutableStateOf(false) }
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(userTypeState.value)
        Box(modifier = Modifier) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "More"
                )
            }
            DropdownMenu(expanded = expanded,
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Customer") },
                    onClick = { userTypeState.value = "Customer" })
                DropdownMenuItem(
                    text = { Text("Pilot") },
                    onClick = { userTypeState.value = "Pilot" })
            }
        }
    }
}

@Composable
fun RatingField(ratingState: MutableState<Int>, isValidRating: MutableState<Boolean>){
    Row(modifier = Modifier
        .background(Color(0xff7c4700))
        .padding(8.dp)){
        for(i in 1..5){
            IconButton(onClick = { ratingState.value = i; isValidRating.value = true }) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "star",
                    tint = if(i > ratingState.value) Color.White else Color(0xffffd700)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, icon: ImageVector, onIconClick: ()->Unit, contentDescription: String){
    TopAppBar(
        title = {Text(text = title)},
        navigationIcon = {
            IconButton(onClick = onIconClick) {
                Icon(imageVector = icon, contentDescription = contentDescription)
            }
        }
    )
}

@Composable
fun MenuHeader(person: Person){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 32.dp)){
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Rounded.Person, contentDescription = "Person", Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${person.firstName} ${person.lastName}", style = TextStyle(fontSize = 18.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "@${person.username}", style = TextStyle(fontSize = 14.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "  ${person.category}  ", style = TextStyle(fontSize = 14.sp), modifier = Modifier.background(Color(0xFF808080)))
        }
    }
}

@Composable
fun MenuRow(menuItem: MenuItem, itemTextStyle: TextStyle){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { menuItem.onClick() }
        .padding(16.dp)){

        Icon(imageVector = menuItem.icon, contentDescription = menuItem.title)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = menuItem.title, style=itemTextStyle, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MenuBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp)
){
    LazyColumn(modifier){
        items(items){
            item -> MenuRow(menuItem = item, itemTextStyle)
        }
    }
}

@Composable
fun SellerRow(base64String: String, name: String, f:()->Unit){
    val b64 = base64String.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(BorderStroke(2.dp, Color.Black))
        .clickable { f() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Image(bitmap = decodedImage.asImageBitmap(), contentDescription = name, modifier = Modifier
            .weight(0.3f)
            .padding(1.dp))
        Text(text = name, modifier = Modifier
            .weight(0.7f)
            .padding(12.dp), style = TextStyle(fontSize = 40.sp))
    }
}

@Composable
fun ItemRow(item: Item, f:()->Unit){
    val b64 = item.image.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

    val price = "${item.price} EGP"
    val rating = if(item.rating == null) "Not Available" else item.rating.toString() + " (" + item.numberOfRaters + ')'
    val description = if(item.description == "" || item.description == "nan") "Description Not Available" else if (item.description.length < 25) item.description else item.description.substring(0, 25) + "..."
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(BorderStroke(2.dp, Color.Black))
        .clickable { f() },
        verticalAlignment = Alignment.CenterVertically){
        Image(bitmap = decodedImage.asImageBitmap(), contentDescription = item.name, modifier = Modifier
            .weight(0.2f)
            .padding(1.dp))
        Column(modifier = Modifier
            .weight(0.5f)
            .padding(10.dp)) {
            Spacer(Modifier.height(24.dp))
            Text(text = item.name, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Text(text = description, style = TextStyle(fontSize = 12.sp))
            Text(text = "Price: $price")
            // Text(text = "Rating: ${item.rating ?: "Not Available"}", style = TextStyle(fontSize = 14.sp))
            Text(text = "Rating: $rating", style = TextStyle(fontSize = 14.sp))
            Spacer(Modifier.height(24.dp))
        }
        Column(modifier = Modifier
            .weight(0.3f)
            .padding(5.dp)) {
            Button(onClick = { }, modifier = Modifier
                .width(500.dp)
                .height(60.dp)
                ) {
                Text(text = "Add\nto Basket", style = TextStyle(fontSize = 13.sp, textAlign = TextAlign.Center), fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(1f))
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { }, modifier = Modifier
                .width(500.dp)
                .height(60.dp)
                ) {
                Text(text = "Add\nto Shortlist", style = TextStyle(fontSize = 11.sp, textAlign = TextAlign.Center), fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(1f))
            }

        }
    }
}

@Composable
fun ReviewRow(review: Review){
    val borderStroke = if(review.isMine) BorderStroke(6.dp, Color(0xff7c4700)) else BorderStroke(2.dp, Color.Black)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(borderStroke)
        .padding(16.dp),
        horizontalAlignment = Alignment.Start){
        Row() {
            Icon(imageVector = Icons.Rounded.Person, contentDescription = "person")
            Text(text = review.reviewerUsername, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.width(8.dp))
            if (review.isMine) Text(text = " you ", modifier = Modifier.background(Color.Gray))
            Row() { for (i in 1..review.rating) { Icon(imageVector = Icons.Default.Star, contentDescription = "star", tint = Color(0xffffd700)) } }
        }
        Text(text = "        " + review.dateCreated, style = TextStyle(fontSize = 12.sp))
        Text(text = "     " + review.text)
    }
}

@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onConfirmation() }) { Text("Confirm") } },
        dismissButton = { TextButton(onClick = { onDismissRequest() }) { Text("Dismiss") } }
    )
}


/////////////////////////////////////////////////////
@Preview(showBackground = true)
@Composable
fun ShowTestPreview() {
    DeliveryTheme {
       val rating = remember { mutableStateOf(0) }
       val isValid = remember { mutableStateOf(false) }
       RatingField(rating, isValid)
    }
}