Quiz Platform
=============

Quiz Platform is a Open Source knowledge testing platform for Android based on Jetpack Compose and
Google solutions. The project is based on the latest Android development solutions and can act as a
sample for other developers.

Coming soon app "Quiz Platform" on Google Play.

# Apps built on Quiz Platform

500K+ downloads (total for 5 apps)

<a href="https://play.google.com/store/apps/details?id=com.yugyd.biologyquiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>
<a href="https://play.google.com/store/apps/details?id=com.yugyd.russianhistoryquiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>
<a href="https://play.google.com/store/apps/details?id=com.yugyd.sociologyquiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>
<a href="https://play.google.com/store/apps/details?id=com.yugyd.geographyquiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>
<a href="https://play.google.com/store/apps/details?id=com.yugyd.russianlanguagequiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

# Stack

The project is developed strictly within the framework of the Google way, only Google solutions are
used. For novice developers to dive into android development.

* Language: Kotlin
* Architecture: MVVM (Google), clean, multi-module
* UI: Compose, Material 3
* Navigation: Jetpack Compose Navigation
* Threading: Coroutines + Flow
* DI: Hilt
* DB: Room
* Image: Coil
* Firebase: Analytics, AppIndexing, Crashlytics, Cloud Messaging, Remote Config
* Logging: Timber
* Testing: Coming soon

# Build types

The devDebug build option can be built and run. The release option is used on internal projects and
is not publicly available, you need a real Firebase project to access it.

Debug - Logging, debug mode, proguard off.

Release - No logging, no debug mode, proguard enabled.

# Deploy to Firebase App Distribution

Use environment variable `FIREBASE_APP_ID`. Set your app id value from Firebase.

https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_environment_variables

# Contributions

[Guide](docs/CONTRIBUTION.md)

# Build

## Deploy debug to Firebase App Distribution

- `./gradlew clean bundleDevDebug`
- `./gradlew appDistributionUploadDevDebug`

## Deploy release to Firebase App Distribution

- `./gradlew clean bundleDevRelease`
- `./gradlew appDistributionUploadDevRelease`

## Upload to Google Play

- `./gradlew clean bundleProdRelease`
- Manual upload to Google Play

# License

```
   Copyright 2023 Roman Likhachev

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
