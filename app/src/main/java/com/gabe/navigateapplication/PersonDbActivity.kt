package com.gabe.navigateapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
                    val shape = CircleShape
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(26.dp)
                    ) {
                        PersonListScreen()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                                .border(2.dp, MaterialTheme.colors.secondary, shape)
                                .background(Color.White, shape)
                                .padding(6.dp)
                                .clickable { onHomeClick() },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "home activity",
                                tint = Color.Blue
                            )
                            Text(text = "点第一行去主页")
                        }
                    }
                }
            }
        }
    }

    private fun onHomeClick() {
        this@PersonDbActivity.startActivity(
            Intent(this@PersonDbActivity, MainActivity::class.java)
        )
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