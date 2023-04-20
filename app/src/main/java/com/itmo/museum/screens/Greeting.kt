package com.itmo.museum.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itmo.museum.ui.theme.MuseumTheme

@Composable
fun GreetingScreen(
    onLoginClick: (String) -> Unit = {},
    onSkipLoginClick: (Int) -> Unit = {}
) {
    MuseumTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val username = remember { mutableStateOf(TextFieldValue()) }

            Text(
                text = "Login",
                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Username") },
                value = username.value,
                onValueChange = { username.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = { onLoginClick(username.value.text) },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Login")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = AnnotatedString("Skip login"),
                onClick = onSkipLoginClick,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }
    }
}