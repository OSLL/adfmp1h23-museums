package com.itmo.museum.elements

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itmo.museum.R
import com.itmo.museum.models.*
import com.itmo.museum.util.SemanticKeys
import com.itmo.museum.util.shadow

@Composable
internal fun MuseumCard(
    viewModel: MuseumProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val museumName = uiState.museumDetails.name
    val museumAddress = uiState.museumDetails.address
    val museumImageId = uiState.museumDetails.imageId

    fun shareMuseumInfo() {
        val message = "Я собираюсь посетить $museumName по адресу $museumAddress"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)

        context.startActivity(shareIntent)
    }

    BoxWithShadow {
        Column(
            modifier = Modifier.padding(all = 10.dp)
        ) {
            Image(
                painter = painterResource(id = museumImageId),
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = museumName,
                fontSize = MaterialTheme.typography.body1.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
            )
            Text(
                text = museumAddress,
                fontSize = MaterialTheme.typography.body2.fontSize,
                modifier = Modifier
            )
            Row {
                FixedRatingBar(
                    modifier = Modifier.weight(1f),
                    rating = uiState.rating
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        enabled = uiState.shareButtonState == ShareButtonState.ENABLED,
                        onClick = { shareMuseumInfo() },
                    ) {
                        Text("Share")
                    }
                }
            }
        }
    }
}

@Composable
fun MuseumInfo(museum: MuseumDetails) {
    Text(
        text = museum.info,
        fontSize = MaterialTheme.typography.body1.fontSize,
        fontWeight = FontWeight.Light,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun ReviewList(
    viewModel: MuseumProfileViewModel,
    onAddReviewClick: () -> Unit = {},
    reviews: List<UserReviewDetails>
) {
    val isReviewed by viewModel.isReviewed.collectAsState()
    Text(
        text = "${reviews.size} reviews",
        fontSize = MaterialTheme.typography.h5.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )
    WideButton(
        modifier = Modifier.testTag(stringResource(id = R.string.add_review_button)),
        enabled = isReviewed != IsReviewedState.UNKNOWN,
        text = "Add review",
        onClick = onAddReviewClick
    )
    Column(modifier = Modifier) {
        reviews.forEach {
            Review(
                review = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .testTag(stringResource(id = R.string.review_card))
            )
        }
    }
}

@Composable
fun Review(
    review: UserReviewDetails,
    modifier: Modifier
) {
    BoxWithShadow {
        Column(modifier = modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.default_user),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(40.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = review.userName,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .semantics { set(SemanticKeys.ReviewAuthor, review.userName) }
                )
            }

            FixedRatingBar(rating = Rating(1, review.rating))

            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .semantics { set(SemanticKeys.ReviewText, review.text) },
                text = review.text,
                fontSize = MaterialTheme.typography.body1.fontSize,
                fontWeight = FontWeight.Light,
                color = Color.Black,
            )
        }
    }
}

@Composable
fun WideButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String,
    onClick: () -> Unit = {}
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
    ) {
        Text(
            text = text,
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Light,
            modifier = Modifier
        )
    }
}

@Composable
fun BoxWithShadow(
    content: @Composable BoxScope.() -> Unit
) {
    val radius = remember { mutableStateOf(22f) }
    val shadowBorderRadius = remember { mutableStateOf(22f) }
    val offsetX = remember { mutableStateOf(7f) }
    val offsetY = remember { mutableStateOf(7f) }
    val spread = remember { mutableStateOf(7f) }
    val blurRadius = remember { mutableStateOf(22f) }
    val shadowColor = remember { mutableStateOf(Color.Black) }

    Box(
        modifier = Modifier
            .padding(20.dp)
            .shadow(
                shadowColor.value,
                borderRadius = shadowBorderRadius.value.dp,
                offsetX = offsetX.value.dp,
                offsetY = offsetY.value.dp,
                spread = spread.value.dp,
                blurRadius = blurRadius.value.dp
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(radius.value.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center,
        content = content
    )
}