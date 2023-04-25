package com.itmo.museum.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itmo.museum.R
import com.itmo.museum.models.AppViewModelProvider
import com.itmo.museum.models.Museum
import com.itmo.museum.models.MuseumListViewModel
import com.itmo.museum.models.Rating
import com.itmo.museum.ui.theme.MuseumTheme
import com.itmo.museum.util.SemanticKeys
import com.itmo.museum.util.rememberFlowWithLifecycle
import kotlin.math.ceil
import kotlin.math.floor

private val defaultRating = Rating(1200, 3.96f)

@Preview
@Composable
fun FixedRatingBar(
    modifier: Modifier = Modifier,
    rating: Rating = defaultRating,
) {
    @Composable
    fun emptyStar() {
        Image(
            painter = painterResource(id = R.drawable.baseline_star_outline_24),
            contentDescription = "Empty star"
        )
    }

    @Composable
    fun halfStar() {
        Image(
            painter = painterResource(id = R.drawable.baseline_star_half_24),
            contentDescription = "Half star"
        )
    }

    @Composable
    fun star() {
        Image(
            painter = painterResource(id = R.drawable.baseline_star_24),
            contentDescription = "Star"
        )
    }


    Row(
        modifier = modifier
            .semantics { set(SemanticKeys.RatingBar, rating.average) }
    ) {
        val ratingFloor = floor(rating.average).toInt()
        val ratingCeil = ceil(rating.average).toInt()

        repeat(ratingFloor) { star() }

        if (floor(rating.average) != ceil(rating.average)) {
            when (rating.average - ratingFloor) {
                in (0.0 .. 0.24) -> emptyStar()
                in (0.24 .. 0.74) -> halfStar()
                in (0.74 .. 1.0) -> star()
            }
        }

        repeat(5 - ratingCeil) {
            emptyStar()
        }
    }
}

@Composable
fun MuseumIndexCard(
    modifier: Modifier = Modifier,
    viewModel: MuseumListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    museum: Museum,
    onClick: (String) -> Unit = {}
) {
    val ratingLifecycleAware = rememberFlowWithLifecycle(flow = viewModel.getRatingOf(museum.id))
    val rating by ratingLifecycleAware.collectAsState(initial = Rating())

    MuseumTheme {
        Column(modifier = modifier
            .background(color = Color.White)
            .padding(all = 8.dp)
            .clickable { onClick(museum.name) }
        ) {
            Image(
                painter = painterResource(id = museum.imageId),
                contentDescription = "Museum profile picture",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = museum.name)
            Spacer(modifier = Modifier.height(4.dp))
            FixedRatingBar(rating = rating)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = museum.address)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = museum.info, maxLines = 5)
        }
    }
}

@Composable
fun MuseumAppTopBar(
    titleText: String,
    onBackClicked: () -> Unit,
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
        }
    )
}
