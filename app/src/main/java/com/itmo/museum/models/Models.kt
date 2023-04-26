package com.itmo.museum.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.itmo.museum.R

data class Rating(
    val count: Int = 0,
    val average: Float = 0f
)

@Entity(tableName = "museums")
data class Museum(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val isVisited: Boolean,
    val info: String,
    val imageId: Int,
    val latitude: Double,
    val longitude: Double
) {
    val location: LatLng
        get() = LatLng(latitude, longitude)
}

@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val profilePictureId: Int = R.drawable.default_user
)

@Entity(tableName = "Reviews")
data class UserReview(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val userName: String,
    val museumId: Int,
    val rating: Float,
    val text: String,
)
