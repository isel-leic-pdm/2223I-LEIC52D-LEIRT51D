@file:Suppress("unused")
package palbp.laboratory.demos.quoteofday.utils

import androidx.compose.ui.test.SemanticsNodeInteractionCollection

fun SemanticsNodeInteractionCollection.assertNotEmpty(): SemanticsNodeInteractionCollection {
    fetchSemanticsNodes(
        atLeastOneRootRequired = true,
        errorMessageOnFail = "Failed to assert not empty list of matching nodes"
    )
    return this
}

fun SemanticsNodeInteractionCollection.assertEmpty(): SemanticsNodeInteractionCollection {
    val matchedNodes = fetchSemanticsNodes(atLeastOneRootRequired = false)
    if (matchedNodes.isNotEmpty()) {
        throw AssertionError("Failed to assert empty list of matching nodes")
    }
    return this
}
