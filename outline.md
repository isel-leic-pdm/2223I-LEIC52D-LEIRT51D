
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

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=jteeUsikP4w&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=4)

### 30/09/2022 - Building a UI: navigation
* Navigation between Activities
  * [Intents (explicit and implicit) and intent filters](https://developer.android.com/guide/components/intents-filters)
    * [Sending the user to another app](https://developer.android.com/training/basics/intents/sending)
    * [Common Intents](https://developer.android.com/guide/components/intents-common)
  * [User task and back stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack)
* Demo:
  * Adding a credits screen to the Quote Of Day demo (still with a fake service)

* Other links:
  * [Using material components and layouts](https://developer.android.com/jetpack/compose/layouts/material)
  
[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=Nny3X-En0p4&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=5)

## Week 4
* Practical classes dedicated to the development of the course's project

## Week 5
### 12/10/2022 - Comunication with HTTP APIs
* The Android device as an HTTP client
  * Motivation
  * Consequences of distribution
  * [Required permissions](https://developer.android.com/training/basics/network-ops/connecting)
* HTTP comunication with [OkHttp](https://square.github.io/okhttp/)
  * Programming model
  * Making asynchronous requests 
  * Bridging between OkHttp's and Kotlin's concurrency models
* JSON serialization and desserialization with [Gson](https://github.com/google/gson)
* Demo:
  * Quote Of Day with a real service (fake service is no more)

For reference:
* [Kotlin coroutines on Android](https://developer.android.com/kotlin/coroutines)

Other links:
* [Quote Of Day API repo](https://github.com/palbp/laboratory/tree/main/Mobile/Demos/QuoteOfDay/server)
* [Siren media type](https://github.com/kevinswiber/siren)
* [ngrok](https://ngrok.com/)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=cE5pJ6L6m80&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=6)

### 14/10/2022 - State management: continued
* Navigation between activities, revisited
  * [Sending data between activities](https://developer.android.com/guide/components/activities/parcelables-and-bundles#sdba)
  * [Parcelable contract](https://developer.android.com/reference/android/os/Parcelable)
  * [Parcelable implementation generator and the `@Parcelize` annotation](https://developer.android.com/kotlin/parcelize)
* Application state vs presentation state, revisited
* Presentation state:
  * Preserving it outside the hosting process: Parcelable contract, again
  * [Restoring state in Jetpack Compose: `rememberSaveable`](https://developer.android.com/jetpack/compose/state#restore-ui-state)
* Demo:
  * Adding a screen to the Quote Of Day demo that displays the week's quotes
    * Build an `ExpandableQuoteView` composable (with presentation state)
    * Use lazy lists instead of the eager versions

For reference:
* [Lists and grids](https://developer.android.com/jetpack/compose/lists)

Other links:
* [Animations in Jetpack Compose](https://developer.android.com/jetpack/compose/animation)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=Iisv0qSfnKo&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=7)

## Week 6
### 19/10/2022

* Practical classes dedicated to the development of the course's project

## Week 7
### 26/10/2022 - State managemenet
* Considerations on the design of Android applications
  * [Guide to app architecture](https://developer.android.com/topic/architecture)
  * Materialization on the QuoteOfDay demo application
* Resource managment 
  * Rationalizing the use of the communications' network
  * Leveraging HTTP caching
  * [Caching support in OkHttp](https://square.github.io/okhttp/features/caching/)
  * [Accessing application specific storage in Android](https://developer.android.com/training/data-storage/app-specific)

Other links:
* [HTTP Caching - MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching)
* [HTTP Caching - RFC 9111](https://httpwg.org/specs/rfc9111.html)

[Lecture video (in Portuguese) _(coming soon)](https://www.youtube.com/watch?v=tu55lJC7D5I&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=8)

### 28/10/2022 - Execution in Android: continued

* [Performing background work (i.e. non user facing)](https://developer.android.com/guide/background)
  * Motivation
  * Resource management in Android: [processes](https://developer.android.com/guide/components/activities/process-lifecycle)

* [Introduction to the Work Manager API](https://developer.android.com/topic/libraries/architecture/workmanager/basics)
  * [Purpose and motivation](https://developer.android.com/topic/libraries/architecture/workmanager/)
  * Building blocks and [concurrency model](https://developer.android.com/topic/libraries/architecture/workmanager/advanced/coroutineworker)

Other links:
* [https://dontkillmyapp.com/](https://dontkillmyapp.com/)
* [Testing Worker implementation](https://developer.android.com/topic/libraries/architecture/workmanager/how-to/testing-worker-impl)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=P4uVTUlTydU&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=9)

## Week 8
### 02/11/2022

* Practical class dedicated to the development of the course's project

### 04/11/2022 - Automatic tests in Android, revisited

* Building automated tests in Android, revisited
  * Testing domain artifacts with JUnit tests
  * Testing artifacts that depend on the Android runtime with instrumented tests
* Test doubles: fakes and mocks, revisited
  * [Using Mocck in Android](https://mockk.io)
* A consensual approach to software development
  * [Test Driven Development (introduction)](https://en.wikipedia.org/wiki/Test-driven_development)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=2Mm3-m8j_rs&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=10)

## Week 9
### 09/11/2022

* Practical class dedicated to the development of the course's project

### 11/11/2022 - Building the TicTacToe application: part 1
* Live coding session:
  * Building the TicTacToe application from scratch, starting with the automated tests
  * Considerations on software design driven by the need to automated testing
* State management, continued:
  * Persistent storage using [shared preferences]((https://developer.android.com/training/data-storage/shared-preferences))

Other links:
* [Working with fonts](https://developer.android.com/jetpack/compose/text#fonts)

[Lecture video (in Portuguese)](https://www.youtube.com/watch?v=OXUiX-ur2dw&list=PL8XxoCaL3dBhGZmXh2_SdA-RdeVxuf8Mv&index=11)