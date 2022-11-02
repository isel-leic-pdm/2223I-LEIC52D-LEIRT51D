@file:Suppress("unused")

package palbp.laboratory.demos.quoteofday.testutils

import androidx.compose.ui.test.SemanticsMatcher
import palbp.laboratory.demos.quoteofday.ui.expandedPropertyKey

/**
 * Checks whether a given composable has the semantic property defined by
 * [expandedPropertyKey], that is, the composable has an 'expanded' state
 */
fun hasExpandableProperty() =
    SemanticsMatcher.keyIsDefined(expandedPropertyKey)

/**
 * Checks whether a given composable has the semantic property defined by
 * [expandedPropertyKey] with the value [true], which indicates that the
 * composable is in its 'expanded' state
 */
fun isExpanded() =
    SemanticsMatcher.expectValue(expandedPropertyKey, true)