package com.example.delivery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.delivery.ui.theme.DeliveryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val token = intent.getStringExtra("Token")
        super.onCreate(savedInstanceState)

        setContent {
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val coroutineScope = rememberCoroutineScope()
            var person by remember { mutableStateOf(Person()) }

            LaunchedEffect(Unit){ coroutineScope.launch { person = token?.let { whoAmI(it) }!! } }

            DrawHomeLayout(drawerState = drawerState, coroutineScope = coroutineScope, token = token!!, person = person)
            }
        }
    }

fun createMenuLists(context: Context, coroutineScope: CoroutineScope, drawerState: DrawerState, token: String, category: String): List<MenuItem>{
    if(category == "Customer")
        return listOf(
            MenuItem("Profile", Icons.Default.Person) { Log.d("home", "profile") },
            MenuItem("Orders", Icons.Default.List) { Log.d("home", "orders") },
            MenuItem("Shop", Icons.Default.Store) { coroutineScope.launch { drawerState.close() }; context.startActivity(Intent(context, ShopSellersActivity::class.java).also { it.putExtra("Token", token) }) },
            MenuItem("Basket", Icons.Default.ShoppingBasket) { Log.d("home", "basket") },
            MenuItem("Shortlist", Icons.Default.Bookmarks) { Log.d("home", "shortlist") },
            MenuItem("Help", Icons.Default.Help) { Log.d("home", "help") },
            MenuItem("Logout", Icons.Default.Logout) { Log.d("home", "logout") }
        )
    else  // Pilot
        return listOf(
            MenuItem("Profile", Icons.Default.Person) { Log.d("home", "profile") },
            MenuItem("Orders", Icons.Default.List) { Log.d("home", "orders") },
            MenuItem("Available Orders", Icons.Default.Money) {Log.d("home", "available-orders") },
            MenuItem("Help", Icons.Default.Help) { Log.d("home", "help") },
            MenuItem("Logout", Icons.Default.Logout) { Log.d("home", "logout") }
        )
}

@Composable
fun DrawHomeLayout(drawerState: DrawerState, coroutineScope: CoroutineScope, person: Person, token: String){
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f)
                    .background(White)
            ){
                MenuHeader(person)
                MenuBody(items = createMenuLists(LocalContext.current, coroutineScope, drawerState, token, person.category))
            }
        }){
        Scaffold(
            topBar = {
                AppBar(
                    title = "Home",
                    icon = Icons.Default.Menu,
                    onIconClick = { coroutineScope.launch { drawerState.open() } },
                    contentDescription = "Home"
                )
                DrawHomeBackLayout()
            },
            content = {it.calculateBottomPadding()}
        ) }

}

@Composable
fun DrawHomeBackLayout(){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Here\nWill\nBe\nHOME", fontSize = 70.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ShowHomePreview() {
    DeliveryTheme {
        DrawHomeBackLayout()
    }
}