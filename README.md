Quiz Platform
=============

Quiz Platform is a Open Source knowledge testing platform for Android based on Jetpack Compose and
Google solutions. The project is based on the latest Android development solutions and can act as a
sample for other developers.

Coming soon app "Quiz Platform" on Google Play.

# Download Quiz Platform

<a href="https://play.google.com/store/apps/details?id=com.yugyd.quiz"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

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

## Build debug

- `./gradlew clean assembleDevDebug`

## Deploy debug to Firebase App Distribution

- `./gradlew clean bundleDevDebug`
- `./gradlew appDistributionUploadDevDebug`

## Deploy release to Firebase App Distribution

- `./gradlew clean bundleDevRelease`
- `./gradlew appDistributionUploadDevRelease`

## Upload to Google Play

- `./gradlew clean bundleProdRelease`
- Manual upload to Google Play

# Build a standalone app

## Code integration

* Switch the `isProductFlavorFilterEnabled` property to `false` in the
  [BuildTypeAndroidApplicationPlugin.kt](build-logic/convention/src/main/kotlin/com/yugyd/buildlogic/convention/buildtype/BuildTypeAndroidApplicationPlugin.kt)
* Switch the `IS_BASED_ON_PLATFORM_APP` property to `true` in the [build.gradle](app/build.gradle)
  file.
* Add the path to the [google-services.json](app/src/dev/google-services.json) file to
  the `isGoogleServicesExists` method in the [build.gradle](app/build.gradle) file.
* Add the [product flavor](https://developer.android.com/build/build-variants#product-flavors)
  implementation in the [build.gradle](app/build.gradle) file. For example, `dev`, which comes by
  default, can be made following its example.
* Add the Firebase App Distribution credential file `firebase-credentials.json` to
  the [secret](secret) directory. For
  example, [firebase-credentials.json](secret/firebase-credentials.json). This step is optional.

## Resource integration

* Add a new directory with the flavor name to [build.gradle](app/src). For
  example, [dev](app/src/dev), the same name will be used in the examples below.
* Add the database file to your flavor's `assets`. Note that the file name must
  be `content-encode-pro.db`. For example, [assets](app/src/dev/assets/content-encode-pro.db)
* Add `strings.xml` to `res/values` of your flavor. This file contains string values specific to
  your application. For example, [res/values](app/src/dev/res/values/strings.xml)
* Add `ad-ids.xml` to `res/values` of your flavor. This file contains ad identifiers for your app.
  For example, [res/values](app/src/dev/res/values/strings.xml). This step is optional.
* Add category images to your flavor's `assets`. Note that the file format must be `jpg` or `png`.
  Example, [assets](app/src/dev/assets/) folder. This step is optional.
* Add a `google-services.json` file to connect to Firebase services in the flavor name directory.
  For example, [google-services.json](app/src/dev/google-services.json). This step is optional.

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
