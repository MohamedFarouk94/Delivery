package com.example.delivery

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import java.nio.file.WatchEvent

data class MenuItem(val title: String, val icon: ImageVector, val onClick: ()->Unit)

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
        verticalAlignment = Alignment.CenterVertically){
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


/////////////////////////////////////////////////////
@Preview(showBackground = true)
@Composable
fun ShowTestPreview() {
    DeliveryTheme {
        val b64 = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\\nHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\\nMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCACTAMsDASIA\\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iii\\ngAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKA\\nCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAK\\nKKKACiijigAoopOlAC0UUlAC0UUlAC0UUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRT\\nSwAJJ6UALkUhYY61Rn1BVOE596zLi9djhn49BWUqqRpGk5G491DH1cfhVZ9UhXsTXOyXWP4vyqq9\\n2Aepz7msJYh9Dojhu50smtov3UJqP+3Vz/q/1FcvJfDrk4qs94c+x9KyeIkarDROzGvQ55Q1PHrN\\n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\\noA//2Q==\\n"
        val item = Item(id = 10, name = "Beefburger Sandwich", sellerId = 1, sellerUsername = "mac", description = "Very delicious sandwich yummy!",
                        price = 36.5, image = b64, rating = null, numberOfBuyouts = 0, numberOfOrders = 0, numberOfRaters = 0)
        ItemRow(item = item) {
            
        }
    }
}