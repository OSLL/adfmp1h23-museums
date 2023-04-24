package com.itmo.museum.models

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.itmo.museum.MuseumsApp
import com.itmo.museum.R
import com.itmo.museum.data.MuseumRepository
import com.itmo.museum.data.ReviewRepository
import com.itmo.museum.data.UserRepository
import com.itmo.museum.navigation.MuseumAppScreen
import com.itmo.museum.util.rating
import com.itmo.museum.util.toMuseumDetails
import com.itmo.museum.util.toUserReview
import com.itmo.museum.util.toUserReviewDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MuseumListViewModel(
    username: String?,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val museumRepository: MuseumRepository
) : ViewModel() {

    fun getRatingOf(museumId: Int): Flow<Rating> {
        return reviewRepository.getReviewsStream(museumId)
            .map { reviews -> reviews.map { it.toUserReviewDetails() } }
            .map { it.rating }
    }

    private val visitedMuseums: StateFlow<List<Museum>> =
        museumRepository.getVisitedMuseumsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    private val museums: StateFlow<List<Museum>> =
        museumRepository.getAllMuseumsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    val uiState = combine(
        visitedMuseums,
        museums
    ) { visitedMuseums, museums ->
        MuseumListUiState(
            user = username?.let { User(name = it) },
            museumList = museums,
            visitedMuseums = visitedMuseums
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = MuseumListUiState()
    )

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun markAsVisited(museum: Museum) {
        viewModelScope.launch {
            museumRepository.updateMuseum(museum.copy(isVisited = true))
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MuseumListUiState(
    val user: User? = null,
    val museumList: List<Museum> = emptyList(),
    val visitedMuseums: List<Museum> = emptyList()
)

data class MuseumDetails(
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val isVisited: Boolean = false,
    val info: String = "",
    val imageId: Int = R.drawable.hermitage,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    val location: LatLng = LatLng(latitude, longitude)
}

data class UserReviewDetails(
    val id: Int = 0,
    val userId: Int,
    val userName: String,
    val museumId: Int,
    val rating: Float,
    val text: String
)

data class UserDetails(
    val id: Int = 0,
    val name: String,
    val profilePictureId: Int,
) {
    companion object {
        val Anonymous = User(
            name = "Anonymous",
            profilePictureId = R.drawable.default_user,
        )
    }
}

class MuseumProfileViewModel(
    username: String?,
    museumId: Int,
    museumRepository: MuseumRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val reviews: StateFlow<List<UserReviewDetails>> =
        reviewRepository.getReviewsStream(museumId)
            .filterNotNull()
            .map { reviews ->
                reviews.map { it.toUserReviewDetails() }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    private val museumProfileState: StateFlow<MuseumDetails> =
        museumRepository.getMuseumStream(museumId)
            .filterNotNull()
            .map { it.toMuseumDetails() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MuseumDetails()
            )

    val uiState: StateFlow<MuseumProfileUiState> = combine(
        reviews,
        museumProfileState
    ) { reviews, museumDetails ->
        MuseumProfileUiState(
            user = username?.let { User(name = it) } ?: UserDetails.Anonymous,
            reviews = reviews,
            rating = reviews.rating,
            museumDetails = museumDetails
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = MuseumProfileUiState(
            user = username?.let { User(name = it) } ?: UserDetails.Anonymous,
            museumDetails = MuseumDetails(id = museumId)
        )
    )

    fun addReview(review: UserReviewDetails) {
        viewModelScope.launch {
            reviewRepository.insertReview(review.toUserReview())
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MuseumProfileUiState(
    val user: User,
    val reviews: List<UserReviewDetails> = emptyList(),
    val rating: Rating = Rating(),
    val museumDetails: MuseumDetails = MuseumDetails()
)

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MapState()
    )
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            lastKnownLocation = task.result ?: HERMITAGE_LOCATION
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("loc", "Failed to get device location")
        }
    }
}

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MuseumListViewModel(
                museumApplication().applicationContext.getUsername(),
                museumApplication().container.reviewRepository,
                museumApplication().container.userRepository,
                museumApplication().container.museumRepository
            )
        }
        initializer {
            val savedStateHandle = this.createSavedStateHandle()
            val museumId: Int =
                checkNotNull(savedStateHandle[MuseumAppScreen.WithArgs.MuseumProfile.museumIdArg])
            MuseumProfileViewModel(
                museumApplication().applicationContext.getUsername(),
                museumId,
                museumApplication().container.museumRepository,
                museumApplication().container.reviewRepository
            )
        }
        initializer {
            MuseumSearchViewModel(
                museumApplication().container.museumRepository
            )
        }
    }

    private fun Context.getUsername(): String? {
        val preferences = getSharedPreferences(
            getString(R.string.user_preferences_file_key),
            Context.MODE_PRIVATE
        )
        return preferences.getString(
            getString(R.string.username_key),
            null
        )
    }
}

fun CreationExtras.museumApplication(): MuseumsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MuseumsApp)

data class MapState(val lastKnownLocation: Location = HERMITAGE_LOCATION)

private val HERMITAGE_LOCATION = Location("").apply {
    latitude = 59.93989616491988
    longitude = 30.314559697617607
}
