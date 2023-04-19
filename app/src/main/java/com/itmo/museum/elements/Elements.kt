package com.itmo.museum.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.itmo.museum.R
import com.itmo.museum.models.Museum
import com.itmo.museum.models.Rating
import com.itmo.museum.ui.theme.MuseumTheme
import kotlin.math.ceil
import kotlin.math.floor

private val defaultRating = Rating(1200, 3.96)

@Preview
@Composable
fun RatingBar(rating: Rating = defaultRating) {
    Row {
        repeat(floor(rating.average).toInt()) {
            Image(painter = painterResource(id = R.drawable.baseline_star_24), contentDescription = "")
        }

        if (floor(rating.average) != ceil(rating.average)) {
            if (rating.average - rating.average.toInt() >= 0.95) {
                Image(painter = painterResource(id = R.drawable.baseline_star_24), contentDescription = "")
            } else {
                Image(painter = painterResource(id = R.drawable.baseline_star_half_24), contentDescription = "")
            }
        }

        repeat(5 - ceil(rating.average).toInt()) {
            Image(painter = painterResource(id = R.drawable.baseline_star_outline_24), contentDescription = "")
        }
    }
}

@Preview
@Composable
fun MuseumIndexCard(
    modifier: Modifier = Modifier,
    museum: Museum = defaultMuseum
) {
    MuseumTheme {
        Surface(modifier = modifier) {
            Column(modifier = Modifier
                .padding(all = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = museum.imageId),
                    contentDescription = "Museum profile picture",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = museum.name)
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(museum.rating)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = museum.address)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = museum.info, maxLines = 5)
            }
        }
    }
}

@Composable
fun MuseumAppTopBar(
    titleText: String,
    onBackClicked: () -> Unit,
    onAboutClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(text = titleText) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onAboutClicked) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "About"
                )
            }
        }
    )
}
