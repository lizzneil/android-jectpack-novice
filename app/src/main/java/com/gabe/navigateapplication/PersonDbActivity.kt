package com.gabe.navigateapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gabe.navigateapplication.ui.PersonListScreen
import com.gabe.navigateapplication.ui.theme.NavigateApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDbActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigateApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        PersonListScreen()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            ) {

                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    this@PersonDbActivity.startActivity(
                                        Intent(this@PersonDbActivity, MainActivity::class.java)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = "home activity",
                                    tint = Color.Black
                                )
                            }
                            Text(text = "点小房子图标去主页")
                        }
                    }
                }
            }
        }
    }
}


// ViewModel.getAction().observe(this, new Observer<Action>() {
//    @Override
//    public void onChanged(@Nullable final Action action) {
//        if(action != null){
//            handleAction(action);
//        }
//    }
//});
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigateApplicationTheme {
        Greeting("Android")
    }
}