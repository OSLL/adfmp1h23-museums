package com.itmo.museum.elements

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gowtham.ratingbar.RatingBar
import com.itmo.museum.models.*
import com.itmo.museum.screens.BottomBar
import com.itmo.museum.ui.theme.MuseumTheme

@Composable
fun AddReview(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBackClicked: () -> Unit,
    viewModel: MuseumProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var rating by remember { mutableStateOf(5f) }
    var reviewText by remember { mutableStateOf(TextFieldValue()) }

    MuseumTheme {
        Scaffold(
            topBar = {
                MuseumAppTopBar(
                    titleText = "Add review",
                    onBackClicked = onBackClicked,
                )
            },
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(modifier.padding(20.dp)) {
                    RatingBar(
                        value = rating,
                        onValueChange = { rating = it },
                        onRatingChanged = {}
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(state = ScrollState(0)),
                        placeholder = { Text(text = "Enter your review") },
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        maxLines = 10,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val review = UserReviewDetails(
                                userId = uiState.user.id,
                                userName = uiState.user.name,
                                museumId = uiState.museumDetails.id,
                                rating = rating,
                                text = reviewText.text
                            )
                            viewModel.addReview(review)
                            onBackClicked()
                        }
                    ) {
                        Text(text = "Post review")
                    }
                }
            }
        }
    }
}
