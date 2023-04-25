package com.itmo.museum

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.itmo.museum.util.SemanticKeys
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@ExperimentalComposeUiApi
@ExperimentalTestApi
@RunWith(Enclosed::class)
class MuseumAppUiTests {

    @RunWith(AndroidJUnit4::class)
    class ReviewTests : AbstractMuseumUiTestWithLogin() {
        @Test
        fun testAddReview(): Unit = composeTestRule.run {
            val museum = museums.first()
            val reviewText = "This is some review text"

            // go to museum profile
            onNodeWithTag(context.getString(R.string.museum_list))
                .performScrollToNode(
                    SemanticsMatcher
                        .expectValue(SemanticKeys.MuseumCard, "museum-${museum.name}")
                )

            onNode(
                SemanticsMatcher
                    .expectValue(SemanticKeys.MuseumCard, "museum-${museum.name}")
            ).performClick()

            // click 'add review' button
            onNodeWithTag(context.getString(R.string.add_review_button)).performClick()

            // give 3 star rating (by clicking at center)
            onNodeWithTag(context.getString(R.string.rating_bar_review))
                .performMouseInput { moveTo(center) }
                .performClick()

            // input review text
            onNodeWithTag(context.getString(R.string.review_text_input))
                .performTextInput(reviewText)

            // post review
            onNodeWithTag(context.getString(R.string.post_review_button))
                .performClick()

            // make sure the review is posted in the profile
            onAllNodesWithTag(context.getString(R.string.review_card))
                .onLast()
                // check review text
                .apply {
                    onChildren()
                        .filterToOne(
                            SemanticsMatcher.expectValue(
                                SemanticKeys.ReviewText,
                                reviewText
                            )
                        )
                        .assertExists()
                }
                .apply {
                    // check review star rating
                    onChildren()
                        .filterToOne(SemanticsMatcher.expectValue(SemanticKeys.RatingBar, 3f))
                        .assertExists()
                }
        }

        @Test
        fun testTryAddMoreThanOneReview(): Unit = composeTestRule.run {
            val museum = museums[1]
            onNodeWithTag(context.getString(R.string.museum_list))
                .performScrollToNode(
                    SemanticsMatcher
                        .expectValue(SemanticKeys.MuseumCard, "museum-${museum.name}")
                ).performClick()
            onNode(
                SemanticsMatcher
                    .expectValue(SemanticKeys.MuseumCard, "museum-${museum.name}")
            ).performClick()
            onNodeWithTag(context.getString(R.string.add_review_button)).performClick()
        }
    }

    class AboutPageTests : AbstractMuseumUiTestWithLogin() {
        @Test
        fun testAboutPage(): Unit = composeTestRule.run {
            onNodeWithText("About")
                .performClick()

            // make sure app authors are specified
            onNodeWithTag(context.getString(R.string.about_page_info))
                .assertTextContains(context.getString(R.string.author_1), substring = true)
                .assertTextContains(context.getString(R.string.author_2), substring = true)
        }
    }

    class GreetingPageTests : AbstractMuseumUITest() {
        @Test
        fun skipLoginTest(): Unit = composeTestRule.run {
            onNodeWithText("Skip login")
                .performClick()

            postReview("Государственный Эрмитаж", "Some review text")

            // check review author is 'Anonymous' since logging in was skipped
            onAllNodesWithTag(context.getString(R.string.review_card))
                .onLast()
                .onChildren()
                .filterToOne(
                    SemanticsMatcher.expectValue(
                        SemanticKeys.ReviewAuthor,
                        "Anonymous"
                    )
                )
                .assertExists()
        }

        @Test
        fun loginWithANameTest(): Unit = composeTestRule.run {
            val username = "Ivan"
            onNodeWithTag(context.getString(R.string.username_input))
                .performTextInput(username)

            onNodeWithTag(context.getString(R.string.login_button))
                .performClick()

            postReview("Государственный Эрмитаж", "Some review text")

            // check review author is 'Anonymous' since logging in was skipped
            onAllNodesWithTag(context.getString(R.string.review_card))
                .onLast()
                .onChildren()
                .filterToOne(
                    SemanticsMatcher.expectValue(
                        SemanticKeys.ReviewAuthor,
                        username
                    )
                )
                .assertExists()
        }

        private fun ComposeTestRule.postReview(museumName: String, reviewText: String) {
            onNodeWithTag(context.getString(R.string.museum_search_input))
                .performTextInput(museumName)

            onNodeWithTag(context.getString(R.string.museum_list))
                .onChildren()
                .filter(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .onFirst()
                .assertExists()
                .performClick()

            onNodeWithTag(context.getString(R.string.add_review_button)).performClick()

            onNodeWithTag(context.getString(R.string.review_text_input))
                .performTextInput(reviewText)

            onNodeWithTag(context.getString(R.string.post_review_button))
                .performClick()
        }
    }
}