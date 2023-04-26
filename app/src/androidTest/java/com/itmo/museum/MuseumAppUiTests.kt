package com.itmo.museum

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.itmo.museum.util.SemanticKeys
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@ExperimentalComposeUiApi
@ExperimentalTestApi
@RunWith(Enclosed::class)
class MuseumAppUiTests {

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

    class MuseumSearchTests : AbstractMuseumUiTestWithLogin() {
        @Test
        fun testNoSearchResults(): Unit = composeTestRule.run {
            inputSearchText("some text")

            // make sure no museums are shown
            onNodeWithTag(context.getString(R.string.museum_list))
                .assertDoesNotExist()

            // make sure a 'No matches found' message is shown
            onNodeWithText("No matches found")
                .assertExists()
        }

        @Test
        fun testSearchWithSingleResult(): Unit = composeTestRule.run {
            val query = "Государственный Эрмитаж"
            inputSearchText(query)

            onNodeWithTag(context.getString(R.string.museum_list), useUnmergedTree = true)
                .onChildren()
                // make sure exactly one result is shown
                .filterToOne(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertExists()
                // make sure this result actually corresponds to the search query
                .onChildren()
                .filterToOne(SemanticsMatcher.expectValue(SemanticKeys.MuseumCardName, query))
                .assertExists()
        }

        @Test
        fun testSearchWithMultipleResults(): Unit = composeTestRule.run {
            val query = "Дворец"
            inputSearchText(query)

            onNodeWithTag(context.getString(R.string.museum_list))
                .onChildren()
                // make sure two results are shown
                .filter(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertCountEquals(2)
        }

        @Test
        fun testClearSearchInputButton(): Unit = composeTestRule.run {
            val query = "some text"

            // input some query
            inputSearchText(query)

            // make sure no results are shown for the query
            onNodeWithTag(context.getString(R.string.museum_list))
                .assertDoesNotExist()

            // click on 'clear' button
            onNodeWithTag(context.getString(R.string.clear_input_button))
                .performClick()

            // make sure all 10 museums are shown since the input query has been cleared
            onNodeWithTag(context.getString(R.string.museum_list))
                .onChildren()
                .filter(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertCountEquals(10)
        }
    }

    class VisitedPageTests : AbstractMuseumUiTestWithLogin() {
        @Test
        fun markSingleMuseumAsVisited(): Unit = composeTestRule.run {
            val museumName = "Государственный Эрмитаж"
            markAsVisited(museumName)

            clickBottomBarItem("Visited")

            onNodeWithTag(context.getString(R.string.museum_list), useUnmergedTree = true)
                .onChildren()
                // make sure visited page contains only one museum
                .filterToOne(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertExists()
                // make sure this museum is actually the one we marked as visited
                .onChildren()
                .filterToOne(SemanticsMatcher.expectValue(SemanticKeys.MuseumCardName, museumName))
                .assertExists()
        }

        @Test
        fun markMultipleMuseumsAsVisited(): Unit = composeTestRule.run {
            markAsVisited("Музей Фаберже")
            markAsVisited("Российский этнографический музей")

            clickBottomBarItem("Visited")

            onNodeWithTag(context.getString(R.string.museum_list))
                .onChildren()
                // make sure visited page contains two museums
                .filter(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertCountEquals(2)
        }

        private fun ComposeTestRule.markAsVisited(museumName: String) {
            // find a museum
            inputSearchText(museumName)

            // go to the museum profile
            onNodeWithTag(context.getString(R.string.museum_list))
                .onChildren()
                // make sure exactly one result is shown
                .filterToOne(SemanticsMatcher.keyIsDefined(SemanticKeys.MuseumCard))
                .assertExists()
                .performClick()

            // click 'mark as visited'
            onNodeWithTag(context.getString(R.string.mark_as_visited_button))
                .performClick()

            // get back to museum list page
            clickBottomBarItem("Museums")
        }

        private fun ComposeTestRule.clickBottomBarItem(itemName: String) {
            onAllNodesWithTag(context.getString(R.string.bottom_bar_item))
                .filterToOne(SemanticsMatcher.expectValue(SemanticKeys.BottomBarItem, itemName))
                .performClick()
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
            inputSearchText(museumName)

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
