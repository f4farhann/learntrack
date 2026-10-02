# LearnTrack

A small Learning Dashboard app for Android, built with Kotlin and Jetpack Compose.
Login → Course list → Course details → Mark lessons complete, with offline support.

**Demo video:** [https://drive.google.com/file/d/1_arY1pzw-vNqqdd4KnE_nRf0x1kT4FnB/view?usp=drive_link] | **APK:** [https://drive.google.com/file/d/1DFes1b-XjMBG8LAzWBJYnmQaJ-5j71n8/view?usp=drive_link]

**Login:** any valid email and a password of 6+ characters. Use `fail@test.com` to see the login error state.

## 1. Architecture

I used MVVM with a Repository layer and Hilt for dependency injection. Data flows one way: Room → Repository → ViewModel → UI, and user actions go back up as function calls.

I chose it because it is simple enough for a small app but still matches how production apps are built. The UI only renders state, the ViewModels only depend on repository interfaces, and the repository decides where data comes from. This made testing easy: I could test the dashboard ViewModel with a fake repository. I kept it lean on purpose (no use-case layer, no feature modules) and would add those as the app grows.

## 2. Offline Support

Room is the single source of truth. The UI never reads the JSON directly, it only observes Room.

On launch the repository loads `courses.json` (my mock API, behind a `CourseDataSource` interface) and saves it into Room. If that refresh fails, the cached courses still show, with a small "Offline: showing saved data" banner. On a fresh install with no cache, the user sees the error state with a Retry button.

Since the data is a local file, real airplane mode doesn't break it, so the dashboard has an **Offline switch** that simulates a network failure for the demo.

One edge case I handled: a refresh would normally overwrite the lessons the user completed. I merge them, so a lesson stays completed if it was completed locally.

## 3. Security

In production I would store tokens in `EncryptedSharedPreferences`, or DataStore encrypted with a key from the Android Keystore, never in plain SharedPreferences. I would use short-lived access tokens with a refresh token, clear them on logout, and add certificate pinning. Login here is mocked and returns a fake token that is not stored.

## 4. Scale (1 million users, hundreds of courses)

- **Pagination** with Paging 3, so the app doesn't load every course at once.
- **Sync progress to the server** with WorkManager, with conflict handling, so progress follows the user across devices.
- **HTTP caching and a CDN** for course data, to reduce server load.
- **Modularization** by feature, for faster builds and team scaling.
- **Crash and analytics monitoring** (Crashlytics) plus remote config for safe rollouts.

## 5. Second Platform (iOS)

I would use SwiftUI with the same MVVM structure:

- ViewModels as `@Observable` classes (instead of ViewModel + StateFlow)
- `URLSession` with async/await for the API (instead of Retrofit)
- SwiftData or Core Data as the offline cache (instead of Room)
- `NavigationStack` for navigation, Keychain for tokens, XCTest for tests

The repository and offline-first logic would stay exactly the same.

## Tests

- `ProgressCalculatorTest`: progress updates when a lesson is completed, and 0% for an empty course.
- `DashboardViewModelTest`: cached courses stay visible when refresh fails, and the error state shows when nothing is cached.

## Run

Open in Android Studio, sync Gradle, run the `app` configuration. Run tests with `./gradlew test`.
