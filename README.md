# DailyPulse 📰

A news application built with **Kotlin Multiplatform (KMP)** that fetches business news from the News API and displays them on both Android and iOS platforms.

## 🚀 Features

- **Cross-platform**: Single codebase for Android and iOS
- **News API integration**: Fetches latest business news
- **Offline caching**: SQLDelight database for local storage
- **Pull-to-refresh**: Native refresh on both platforms
- **Modern UI**: Jetpack Compose (Android) and SwiftUI (iOS)

## 🏗️ Architecture

### Core Technologies
- **Kotlin Multiplatform (KMP)** - Cross-platform development
- **Kotlin Coroutines & Flow** - Async operations and reactive streams
- **[KMP-ObservableViewModel](https://github.com/rickclephas/KMP-ObservableViewModel)** - Shared `ViewModel` base class so Kotlin view models integrate with SwiftUI (and AndroidX lifecycle) without a manual Swift wrapper
- **[KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)** - Gradle plugin + runtime that generates Swift-friendly APIs for `Flow` and `suspend` (e.g. `asyncSequence`, `asyncFunction`) from annotated Kotlin APIs
- **Koin** - Dependency injection
- **Ktor** - HTTP client
- **SQLDelight** - Type-safe database
- **BuildKonfig** - API key management

### Project Structure
```
DailyPulse/
├── shared/                    # Shared KMP code
│   ├── articles/             # News feature
│   │   ├── di/              # Feature dependency injection
│   │   ├── presentation/    # ViewModels and UI state
│   │   ├── services/        # Network, persistence, repository
│   │   └── use_cases/       # Business logic
│   ├── di/                  # Shared dependency injection modules
│   ├── db/                  # Database layer (SQLDelight)
│   └── utils/               # Cross-platform utilities
├── androidApp/              # Android-specific code
│   ├── screens/             # Compose UI screens
│   ├── di/                  # Android DI modules
│   └── MainActivity.kt      # Android entry point
└── iosApp/                  # iOS-specific code
    ├── Screens/             # SwiftUI screens
    ├── Navigation/          # Navigation coordinators
    └── iOSApp.swift         # iOS entry point
```

## 🛠️ Setup

### Development Environment
- **Android Studio** with Kotlin Multiplatform plugin (Hedgehog or newer recommended)
- **Xcode** (version 14+ should work, 15+ recommended for iOS development)
- **Kotlin** (see `gradle/libs.versions.toml`; project tracks current Kotlin release, e.g. **2.3.20**)
- **JDK 17+** (required for Kotlin 2.0+)

### Setting Up Kotlin Multiplatform

#### Install KMP Plugin in Android Studio
1. Open **Android Studio**
2. Go to **File → Settings** (Windows/Linux) or **Android Studio → Preferences** (macOS)
3. Navigate to **Plugins**
4. Search for **"Kotlin Multiplatform"**
5. Click **Install** and restart Android Studio

#### Troubleshooting KMP Setup (Optional)
If you encounter issues with KMP setup, you can use the KMP doctor tool:

```bash
# Install Kotlin (includes KMP doctor)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install kotlin

# Run KMP doctor to diagnose issues
kotlin doctor
```

*Note: This is only needed if you experience setup problems - Android Studio handles KMP setup automatically in most cases.*

### 1. Clone and Setup API Key
```bash
git clone <repository-url>
cd DailyPulse
```

Create `local.properties` in the project root:
```properties
NEWS_API_KEY=your_news_api_key_here
```

**Get your free API key at**: [NewsAPI.org](https://newsapi.org/register)

### 2. Build and Run

#### Android
1. Open the project in **Android Studio**
2. Wait for project sync
3. Run the app

#### iOS
1. Open in **Xcode**:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```
2. Add Swift packages if prompted (see **iOS packages** below)
3. Wait for indexing
4. Run the app

## 📱 Platform Implementation

### Android
- **Jetpack Compose**: UI with Material Design 3
- **Navigation Compose**: Simple navigation with enum-based routes
- **KMP-ObservableViewModel**: Shared `ViewModel` with `viewModelScope` (same pattern as AndroidX `ViewModel`)
- **Koin**: Dependency injection with `koinViewModel()`
- **Pull-to-refresh**: Material 3 `pullToRefresh` components

### iOS
- **SwiftUI**: Native iOS UI with TabView
- **NavigationStack**: Coordinator pattern with NavigationPath
- **Async/Await**: Structured concurrency with Task
- **@MainActor**: Main thread safety for UI updates
- **KMP-ObservableViewModel + SwiftUI**: `@StateViewModel` / `@ObservedViewModel` instead of wrapping the Kotlin VM in a separate `ObservableObject`
- **KMP-NativeCoroutines**: `asyncFunction` / `asyncSequence` for `suspend` and `Flow` from Kotlin
- **Pull-to-refresh**: SwiftUI `.refreshable` modifier

### Swift: ViewModel observable library (brief)

The iOS app depends on **[KMP-ObservableViewModel](https://github.com/rickclephas/KMP-ObservableViewModel)** via Swift Package Manager (`KMPObservableViewModelSwiftUI`, `KMPObservableViewModelCore`). A small bridge file declares that the Kotlin base class conforms to the library’s `ViewModel` protocol so SwiftUI property wrappers work.

On the Kotlin side, feature view models extend `com.rickclephas.kmp.observableviewmodel.ViewModel` and use **`MutableStateFlow(viewModelScope, initial)`** from that library so state updates propagate to SwiftUI. **`@NativeCoroutinesState`** on public `StateFlow` properties (from **KMP-NativeCoroutines**) exposes them as regular Swift properties (e.g. `viewModel.contentState`) instead of raw Obj-C `StateFlow` types.

Together: **ObservableViewModel** owns lifecycle and observation wiring; **NativeCoroutines** shapes the generated Swift API for flows and suspend functions.

## 🏛️ Architecture Patterns

### Dependency Injection with Koin
```kotlin
val articlesModule = module {
    single<ArticlesRemoteDataService> { ArticlesRemoteDataService(get()) }
    single<ArticlesDataSource> { ArticlesDataSource(get()) }
    single<ArticlesRepository> { ArticlesRepository(get(), get()) }
    single<ListArticleUseCase> { ListArticleUseCase(get()) }
    single<ArticlesViewModel> { ArticlesViewModel(get()) }
}
```

### Repository Pattern
```kotlin
class ArticlesRepository(
    private val dataSource: ArticlesDataSource,
    private val remoteDataService: ArticlesRemoteDataService
) {
    suspend fun getArticles(forceFetch: Boolean): List<ArticleModel> {
        // Implements caching strategy
    }
}
```

### State Management (shared ViewModel)
Kotlin view models subclass **KMP-ObservableViewModel**’s `ViewModel` and use its `viewModelScope` and `MutableStateFlow(viewModelScope, …)`. **`@NativeCoroutinesState`** marks `StateFlow`s that Swift should consume as generated properties.

```kotlin
class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : ViewModel() {

    private val _contentState = MutableStateFlow(viewModelScope, ArticlesState(loading = true))

    @NativeCoroutinesState
    val contentState: StateFlow<ArticlesState> = _contentState
}
```

```swift
import SwiftUI
import shared
import KMPObservableViewModelSwiftUI
import KMPNativeCoroutinesAsync

struct ArticlesScreen: View {
    @StateViewModel var viewModel = ArticlesInjector().viewModel

    var body: some View {
        // viewModel.contentState is driven by Kotlin StateFlow + NativeCoroutines
        Text("\(viewModel.contentState.articles.count)")
    }
}
```

## 🗄️ Database

### SQLDelight Configuration
```kotlin
sqldelight {
    databases {
        create(name = "DailyPulseDatabase") {
            packageName.set("com.eli.examples.dailypulse.db")
        }
    }
}
```

## 🌐 Network Layer

### Ktor HTTP Client
```kotlin
class ArticlesRemoteDataService(
    private val httpClient: HttpClient,
    private val configuration: ArticlesConfiguration = ArticlesConfiguration.DEFAULT_CONFIG
) {
    suspend fun fetchArticles(): List<ArticleRemoteItem> {
        val response: ArticlesResponse = httpClient.get(allArticlesURL).body()
        return response.articles
    }
}
```

## 📦 Dependencies

Versions are defined in **`gradle/libs.versions.toml`** and applied from the version catalog in Gradle.

### Core (KMP `shared` module)
- **Kotlin**: 2.3.20
- **Kotlinx Coroutines**: 1.10.1
- **KMP-NativeCoroutines** (plugin + `kmp-nativecoroutines-core`): 1.0.2
- **KMP-ObservableViewModel** (`kmp-observableviewmodel-core`): 1.0.3
- **Kotlinx Serialization JSON**: 1.8.1
- **Ktor**: 2.3.5
- **Koin**: 4.0.4
- **SQLDelight**: 2.0.2

### iOS (Swift Package Manager, Xcode)
- **[KMP-ObservableViewModel](https://github.com/rickclephas/KMP-ObservableViewModel)** — e.g. `1.0.3` (`KMPObservableViewModelSwiftUI`, `KMPObservableViewModelCore`)
- **[KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)** — Async / Combine helpers that match the Gradle plugin (e.g. `KMPNativeCoroutinesAsync`)

### Android
- **Jetpack Compose**: 1.5.4
- **Material Design 3**: 1.3.0
- **Navigation Compose**: 2.8.9
- **Coil**: 2.5.0

---

**Built with ❤️ using Kotlin Multiplatform**
