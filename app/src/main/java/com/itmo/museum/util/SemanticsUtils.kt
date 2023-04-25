package com.itmo.museum.util

import androidx.compose.ui.semantics.SemanticsPropertyKey

object SemanticKeys {
    val RatingBar = SemanticsPropertyKey<Float>("rating")
    val ReviewAuthor = SemanticsPropertyKey<String>("review_author")
    val ReviewText = SemanticsPropertyKey<String>("review_text")
    val MuseumCard = SemanticsPropertyKey<String>("museum_card")
}
