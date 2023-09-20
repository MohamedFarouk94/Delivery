package com.example.delivery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
fun MenuHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 64.dp)){
        Text(text = "   Header", fontSize = 40.sp)
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