package com.example.delivery

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.delivery.ui.theme.DeliveryTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val token = intent.getStringExtra("Token")
            if (token != null) Log.d("home", token)
            DrawHomeLayout()
        }
    }
}


@Composable
fun DrawHomeLayout(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text("Home")
    }
}

@Preview(showBackground = true)
@Composable
fun ShowHomePreview() {
    DeliveryTheme {
        DrawHomeLayout()
    }
}