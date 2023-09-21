package com.example.delivery

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.DeliveryTheme

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
fun SellerRow(base64String: String, name: String){
    val b64 = base64String.replace("\\n", "\n")
    val imageBytes = Base64.decode(b64, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).border(BorderStroke(2.dp, Color.Black)), verticalAlignment = Alignment.CenterVertically){
        Image(bitmap = decodedImage.asImageBitmap(), contentDescription = name, modifier = Modifier.weight(0.3f).padding(1.dp))
        Text(text = name, modifier = Modifier.weight(0.7f).padding(12.dp), style = TextStyle(fontSize = 40.sp))
    }
}


/////////////////////////////////////////////////////
@Preview(showBackground = true)
@Composable
fun ShowTestPreview() {
    DeliveryTheme {
        // MenuHeader(Person(firstName = "Mohamed", lastName = "Farouk", username = "farouk", category = "Customer"))
        var b64 = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\\nHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\\nMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCABoAHcDASIA\\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iii\\ngCGaZLeJpJGCooyTXNXmsT3DERsY4vRTzTtYvftFx5SH93Hx9TU2iWCyk3Eqgqp+QdjXyGNxtfH4\\nr6nhXaK3f5/I9GlShRp+1qIzYru4iYMk0gP1zW/puqrd/upQFl7ejVbvbOO8hKMADj5W9DXJESQT\\nEcq6H9awq/WsmrRbnzQf9feXH2eKi9LM7eiqWnXgvLYOfvjhh6GrtfY0a0K1NVIbM82UXF8rE+tY\\n2pax5RMFvgv0LdhU+r332W32Rn96/T2HrXO2tu11cpEvVjyfQd6+eznNKkJrCYb4n/Vjsw2HTXtJ\\n7A1zcOdzTSkj/aq/Y6zLCwSdjJH69xW/DbRW8IijQBQOmOtc9q9itrMJIwBE/b0NefXwONy6CxUK\\n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\\nj//Z\\n"
        // var b64 = "/9j/4AAQSkZJRgABAQEASABIAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\\nHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\\nMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAEAAQADASIA\\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iii\\ngAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKA\\nCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAK\\nKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAopKWgAooooAKKKKACiiigAooooAKKKKACi\\niigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAGk8dvqa43xb8Q9O8LP9mKNdXrLuECM\\nBtHqxPSuwY4Q/QmvlHV7ye+1a7urhiZZZmZgTz14GfQDgV14Sgq0ve2RjVm4LQ9Hh+Nmoi6zPpNs\\n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\\nVqdROcm0eY+F/Bej+JbNp4by9Qo210bbwcZPbn611Nt8K9DiKmV7iUjsz4H44Aro/D/h+08O2P2S\\n0LlSxYlzk8+9a+R0HWiFJW1QsTmVadR8k3Yx9M8MaRpAzZ2cSNjG7HP5nmk1vw1p2vQCK8iDbOVY\\nHBU+xrZ757Vm63JeppVy2nqHuhGfLUnqe1aOKtY441qjqKXNqeN+MPDukeHZFgtbyea5bkRsVO0e\\npwOlYtpaWSWX2q/e4VJH2xLCOSRyxOewOBxzmtPT/C2v67qRFzbTpuYNLPcDH1xkZJ9hxXpN98PN\\nMvdGtrEGSI2wISRepz1znrkjNcapObbsfVyzKGHpQpynd9WeXaDrEeja8k1naC5XeEUSj95zwMdh\\nk8dK9m1/w3beJ9MigumkiwQ6smNynHuD61U0fwLpOnWdtHLAlxNC+9ZmUBt2cg8da6kEBQO1b0qb\\nirM8PMcfGvVU6fQp6Zp8OlafDZ24xFCu1cnnjvXkfxM0q5t9fN+ysbeZFUPgkKRngnoM9fzr2nIx\\n6VXvLK3vYDDcRJIjDlWXIP51c4cysc+Cxjw1f2rVzynwv400q20yPTtTtFQBQu8LuVuO/p+tbVtr\\nHgGyn+1Qi0SbOQyryM+n/wBap7/4W6RcyF7eSa2zztQ8D8DWevwkh3fNqkxXthFFZWqLSx6U62Cq\\nty5mr9DE8aeMdM1y3+z2dmWKnid1xt9cd81vfCrR7q0hub+ZGSKcBY1YYLAE849Oa2dL+HGiadIs\\nsivdSqcq0zZAP0GBn8K6+KJIUCIuFHAA7VUKbvzTMMVj6SofVqC08zl/EngbT/EU4uZXkhuAu3fG\\nRyPfIOa5/wD4VHaHrqNzz7L/AIV6XikOK0dOL3OOlmGJox5YSsjzyH4S6WrZmvLqQddpKgfoM/rV\\nu5+F2hzQhIjPC6jh0bJP55zXcYyc/wBaU/Sl7KHYbzHFN352ebxfCXTxIDJfXTIDkqNo/MgVZvPh\\nVpMzBraee3I4IQgg/XINd+R7UgGP/wBdHs4dg/tLE3vzs4zQvh3pmjXRuWkkuZcbQZcYUEdgB6et\\nVL/4WaVdXbTQzT26MdxjjIwCeeMg135wO9IMZo9lHaxKx+JUufndzi2+Gmh/2d9lVZA/Xzg+XJ9c\\n96q2nwq0iEP589xOWGAWIG3PpgDmu/8ArSY570eyh2GswxKWk2ecN8JLDeMahdbM5x8uR+ldfoPh\\nyx8P2nkWaHLHLu3JY+pNbNFOMIrVIirjK9aPLOV0cxe+C7C+8TRa1I8gmjwSgxtYjoT3z0710yrh\\nQOfSl44pe1VZLYxnUlOyk9haKKKZAUUUUAFFFFABSEA9hS0UANAA7D8qdQKKAYUUUUAFFFFABRRR\\nQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFA\\nBRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAF\\nFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUU\\nUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAH//Z\\n\""
        b64 = b64.replace("\\n", "\n")
        SellerRow(base64String = b64, name = "McDonald's")
    }
}