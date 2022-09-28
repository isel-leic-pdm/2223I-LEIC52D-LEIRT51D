
## Week 1
### 14/09/2022 - Course introduction

* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources
* Work environment set up and overview of existing tools
* Demo:
    * Hello Android (use the project creation wizard)
    * Guided tour to the project's main artifacts and available tools

For reference:
* [Download Android Studio & App Tools](https://developer.android.com/studio)
* [Android API Levels](https://apilevels.com/)
* [SDK Platform release notes | Android Developers](https://developer.android.com/studio/releases/platforms)

### 16/09/2022 - Execution in Android: introduction

* [Inversion of Control](https://martinfowler.com/bliki/InversionOfControl.html)
    * Motivation and Consequences
    * Library vs Framework: key differences
* [Activity](https://developer.android.com/guide/components/activities/intro-activities)
    * The Activity as an execution host
    * Elementary lifecycle: onCreate, onStart, onStop and onDestroy
    * Threading model

For reference:
* [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
* [Threading model](https://developer.android.com/guide/components/processes-and-threads#Threads)
* [The Kotlin programming language](https://kotlinlang.org/docs/reference/)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=nBL4ynGkFi8&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=1)

## Week 2
### 21/09/2022 - Building a UI with Jetpack Compose: introduction

* @Composable functions: State → @Composable → UI
* Stateless @Composables
  * Elementary composables: `Text` and `Button`
  * Layouts: `Row`, `Column` e `Box`
* Statefull @Composables
  * State management: `remember` and `mutableStateOf`
  * State hoisting
* Execution in Jetpack Compose (concurrency model)
  * In @Composable functions
  * In event handlers
* [Application resources](https://developer.android.com/guide/topics/resources/providing-resources)
* Demo:
  * Quote Of Day (with a fake service)

For reference:
* [Thinking in Compose | Jetpack Compose | Android Developers](https://developer.android.com/jetpack/compose/mental-model)
* [Compose layout basics - Jetpack](https://developer.android.com/jetpack/compose/layouts/basics)
* [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)

Other links:
* [Compose library elements and versions](https://developer.android.com/jetpack/androidx/releases/compose)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=MIR_kGHqNxY&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=2)

### 23/09/2022 - Building a UI with Jetpack Compose: continued
* @Composable functions, continued: State → @Composable → UI
* Statefull @Composables
  * State management: `remember` and `mutableStateOf`
  * State hoisting
* Execution in Jetpack Compose (concurrency model)
  * In @Composable functions
  * In event handlers

For reference:
* [Thinking in Compose | Jetpack Compose | Android Developers](https://developer.android.com/jetpack/compose/mental-model)
* [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=lC2DRGhKCM8&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=3)

## Week 3
### 28/09/2022 - State management: introduction
* [ViewModel: purpose and motivation](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [Lifecycle](https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle)
  * Preserving state across configuration changes using a view model
  * The view model as an alternative execution host
* [Android Application class](https://developer.android.com/reference/android/app/Application)
  * Motivation and lifecycle 
  * Using Application for [manual dependency injection](https://developer.android.com/training/dependency-injection/manual#basics-manual-di)
* [Automated tests in Android: introduction](https://developer.android.com/training/testing)
  * Local tests
  * [Instrumented tests](https://developer.android.com/training/testing/instrumented-tests)
    * [Automated UI tests](https://developer.android.com/training/testing/instrumented-tests/ui-tests)
    * [Testing layouts in Jetpack Compose](https://developer.android.com/jetpack/compose/testing)
* Demo:
  * Lets fix the Quote Of Day demo (still with a fake service)

Lecture video (in Portuguese) (_coming soon_)

### 30/09/2022 - Building a UI: navigation _(preview)_
* Navigation between Activities
  * [Intents (explicit and implicit) and intent filters](https://developer.android.com/guide/components/intents-filters)
    * [Sending the user to another app](https://developer.android.com/training/basics/intents/sending)
  * [User task and back stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack)
* Demo:
  * Adding a credits screen to the Quote Of Day demo (still with a fake service)

* Other links:
  * [Using material components and layouts](https://developer.android.com/jetpack/compose/layouts/material)
  
Lecture video (in Portuguese) (_coming soon_)
