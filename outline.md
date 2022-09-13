
## Week 1
### 14/09/2022 - Course introduction (_preview_)

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

Lecture video (in Portuguese) (_coming soon_)

### 16/09/2022 - Execution in Android: introduction (_preview and script_)

* Step 1: Revisit the Echo Server demo from Concurrent Programming course
    * In this codebase we retain control of most aspects of program execution
* Step 2: Revisit Hello Android demo code
    * Here we don't have direct control over Activity instantation nor on what circumstances our code is executed. 
    That is part of the frameworks' extensibility contract, which we will study in this course. 
* Step 3: Add logging to the activity's lifecycle callbacks to reveal how and when they are executed 
* [Inversion of Control](https://martinfowler.com/bliki/InversionOfControl.html)
    * Motivation and Consequences
    * Library vs Framework: key differences
* [Activity](https://developer.android.com/guide/components/activities/intro-activities)
    * The Activity as an execution host
    * Elementary lifecycle: onCreate, onStart, onStop and onDestroy
    * Threading model
* Step 4: Lets start our running demo - the _Quote Of Day_ application

For reference:
* [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
* [Threading model](https://developer.android.com/guide/components/processes-and-threads#Threads)
* [The Kotlin programming language](https://kotlinlang.org/docs/reference/)
