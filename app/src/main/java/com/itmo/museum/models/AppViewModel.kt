package com.itmo.museum.models

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.itmo.museum.data.MuseumDataProvider
import kotlinx.collections.immutable.*
import kotlinx.coroutines.flow.*

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        AppUiState(
            user = null,
            lastKnownLocation = HERMITAGE_LOCATION,
            museums = MuseumDataProvider.defaultProvider.museums.toPersistentList(),
            reviews = persistentMapOf(),
            rating = persistentMapOf(),
            visitedMuseums = persistentSetOf()
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        // TODO: add placeholder reviews for each museum via addReview()
    }

    fun setUser(user: User) {
        _uiState.update { currentState ->
            currentState.copy(
                user = user
            )
        }
    }

    fun setAnonymousUser() {
        setUser(User.Anonymous)
    }

    fun addVisitedMuseum(museum: Museum) {
        _uiState.update { currentState ->
            currentState.copy(
                visitedMuseums = currentState.visitedMuseums.add(museum)
            )
        }
    }

    fun addReviewFor(museum: Museum, review: UserReview) {
        _uiState.update { currentState ->
            val updatedReviews = currentState.reviews
                .getOrDefault(museum, persistentListOf())
                .add(review)
            val (prevCount, prevAverage) = currentState.rating
                .getOrDefault(museum, Rating())
            val updatedRating = Rating(
                count = prevCount + 1,
                average = (prevAverage + review.rating) / (prevCount + 1)
            )
            currentState.copy(
                reviews = currentState.reviews.put(museum, updatedReviews),
                rating = currentState.rating.put(museum, updatedRating)
            )
        }
    }

    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        lastKnownLocation = task.result ?: HERMITAGE_LOCATION,
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.e("loc", "Failed to get device location")
        }
    }
}

data class AppUiState(
    val user: User?,
    val lastKnownLocation: Location,
    val museums: PersistentList<Museum>,
    val reviews: PersistentMap<Museum, PersistentList<UserReview>>,
    val rating: PersistentMap<Museum, Rating>,
    val visitedMuseums: PersistentSet<Museum>
)

private val HERMITAGE_LOCATION = Location("").apply {
    latitude = 59.93989616491988
    longitude = 30.314559697617607
}
