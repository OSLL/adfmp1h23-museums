package com.itmo.museum.models

import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

    private val _isReviewed = MutableStateFlow(IsReviewedState.UNKNOWN)
    val isReviewed: StateFlow<IsReviewedState> = _isReviewed.asStateFlow()

    fun checkIfReviewed() {
        val userId = uiState.value.user.id
        val museumId = uiState.value.museumDetails.id
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                reviewRepository
                    .getReviewBy(userId, museumId)
                    .collect { review ->
                        if (review == null) {
                            _isReviewed.update { IsReviewedState.IS_NOT_REVIEWED }
                        } else {
                            _isReviewed.update { IsReviewedState.IS_REVIEWED }
                        }
                    }
            }
        }
    }

    private val museumDetails: StateFlow<MuseumDetails> =
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
        museumDetails,
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
        _isReviewed.update { IsReviewedState.IS_REVIEWED }
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

enum class IsReviewedState {
    UNKNOWN,
    IS_NOT_REVIEWED,
    IS_REVIEWED
}

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MapState()
    )
    val uiState: StateFlow<MapState> = _uiState.asStateFlow()

    fun updateLocation(location: Location) {
        _uiState.update {
            it.copy(lastKnownLocation = location)
        }
    }
}

object AppViewModelProvider {
    val Factory = viewModelFactory {
        val profileViewModels = mutableMapOf<Int, MuseumProfileViewModel>()
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
            profileViewModels.getOrPut(museumId) {
                MuseumProfileViewModel(
                    museumApplication().applicationContext.getUsername(),
                    museumId,
                    museumApplication().container.museumRepository,
                    museumApplication().container.reviewRepository
                )
            }
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

data class MapState(val lastKnownLocation: Location? = null)
