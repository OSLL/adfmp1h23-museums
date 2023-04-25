package com.itmo.museum.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itmo.museum.R
import com.itmo.museum.models.MuseumListViewModel
import com.itmo.museum.models.UserState
import com.itmo.museum.ui.theme.MuseumTheme

@Composable
fun GreetingScreen(
    viewModel: MuseumListViewModel,
    onLoginClick: (String) -> Unit = {},
    onSkipLoginClick: (Int) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState()
    val existingUsername = when (val userState = uiState.value.userState) {
        is UserState.NotLoggedIn -> ""
        is UserState.LoggedIn -> userState.username
    }

    MuseumTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var username by remember { mutableStateOf(TextFieldValue(existingUsername)) }

            Text(
                text = "Login",
                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                modifier = Modifier.testTag(stringResource(id = R.string.username_input)),
                label = { Text(text = "Username") },
                value = username,
                onValueChange = { username = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = { onLoginClick(username.text) },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag(stringResource(id = R.string.login_button))
                ) {
                    Text(text = "Login")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                modifier = Modifier.testTag(stringResource(id = R.string.skip_login_button)),
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