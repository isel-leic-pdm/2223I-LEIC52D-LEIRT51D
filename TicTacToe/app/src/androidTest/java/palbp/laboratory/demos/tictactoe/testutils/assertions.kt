@file:Suppress("unused")
package palbp.laboratory.demos.tictactoe.testutils

import androidx.compose.ui.test.*
import palbp.laboratory.demos.tictactoe.ui.IsReadOnly


private fun isReadOnly(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(IsReadOnly)

fun SemanticsNodeInteraction.assertIsReadOnly(): SemanticsNodeInteraction =
    assert(isReadOnly())

fun SemanticsNodeInteraction.assertIsNotReadOnly(): SemanticsNodeInteraction =
    assert(!isReadOnly())
