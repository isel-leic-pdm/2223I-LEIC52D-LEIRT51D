package palbp.laboratory.demos.quoteofday.testutils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun <A : ComponentActivity> createAndroidComposeRule(
    intent: Intent
): AndroidComposeTestRule<ActivityScenarioRule<A>, A> = AndroidComposeTestRule(
    activityRule = ActivityScenarioRule(intent),
    activityProvider = { rule ->
        var activity: A? = null
        rule.scenario.onActivity { activity = it }
        activity!!
    }
)
