package com.itmo.museum.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.R
import com.itmo.museum.models.Museum
import com.itmo.museum.models.Rating
import com.itmo.museum.models.User
import com.itmo.museum.models.UserReview
import com.itmo.museum.util.shadow

// TODO: remove later, this is just a placeholder
internal val placeholderText =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
internal val defaultUser = User(name = "John Johns", profilePictureId = R.drawable.default_user)
internal val defaultReview =
    UserReview(user = defaultUser, rating = 4, text = placeholderText.take(100))
internal val defaultReviews = List(5) { defaultReview }

internal val defaultMuseum =
    Museum("Эрмитаж", "Дворцовая площадь", placeholderText, R.drawable.hermitage, defaultReviews)

@Composable
@Preview
internal fun MuseumCard(
    museum: Museum = defaultMuseum,
) {
    BoxWithShadow {
        Column(
            modifier = Modifier.padding(all = 10.dp)
        ) {
            Image(
                painter = painterResource(id = museum.imageId),
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "ЭРМИТАЖ",
                fontSize = MaterialTheme.typography.body1.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
            )
            RatingBar(
                rating = museum.rating,
            )
        }
    }
}

@Composable
fun MuseumInfo(museum: Museum) {
    Text(
        text = museum.info,
        fontSize = MaterialTheme.typography.body1.fontSize,
        fontWeight = FontWeight.Light,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun ReviewList(reviews: List<UserReview>) {
    Text(
        text = "${reviews.size} reviews",
        fontSize = MaterialTheme.typography.h5.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(10.dp)
    )

    Column(modifier = Modifier) {
        reviews.forEach {
            Review(
                review = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
fun Review(review: UserReview, modifier: Modifier) {
    BoxWithShadow {
        Column(modifier = modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = review.user.profilePictureId),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(40.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = review.user.name,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
            }

            RatingBar(Rating(1, review.rating.toDouble()))

            Text(
                text = review.text,
                fontSize = MaterialTheme.typography.body1.fontSize,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun RouteButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp)
    ) {
        Text(
            text = "Route",
            fontSize = MaterialTheme.typography.h6.fontSize,
            fontWeight = FontWeight.Light,
            color = Color.White,
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