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
- **Koin** - Dependency injection
- **Ktor** - HTTP client
- **SQLDelight** - Type-safe database
- **BuildKonfig** - API key management
- **SKIE** - Kotlin to Swift interop without Native Coroutines plugin

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
- **Kotlin** (project uses 2.0.20/2.1.20, newer versions should be compatible)
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
2. Wait for indexing
3. Run the app

## 📱 Platform Implementation

### Android
- **Jetpack Compose**: UI with Material Design 3
- **Navigation Compose**: Simple navigation with enum-based routes
- **BaseViewModel**: Cross-platform ViewModel with `viewModelScope` for lifecycle management
- **Koin**: Dependency injection with `koinViewModel()`
- **Pull-to-refresh**: Material 3 `pullToRefresh` components

### iOS
- **SwiftUI**: Native iOS UI with TabView
- **NavigationStack**: Coordinator pattern with NavigationPath
- **Async/Await**: Structured concurrency with Task
- **@MainActor**: Main thread safety for UI updates
- **ObservableObject**: Reactive state management
- **SKIE Integration**: Seamless Kotlin Flow to Swift async streams
- **Pull-to-refresh**: SwiftUI `.refreshable` modifier

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

### State Management
```kotlin
// Cross-platform BaseViewModel
expect open class BaseViewModel() {
    val scope: CoroutineScope
}

// Android implementation
actual open class BaseViewModel: ViewModel() {
    actual val scope: CoroutineScope = viewModelScope
}

// iOS implementation  
actual open class BaseViewModel {
    actual val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    fun clear() { scope.cancel() }
}

// Shared ViewModel
class ArticlesViewModel(
    private val listArticleUseCase: ListArticleUseCase
) : BaseViewModel() {
    private val internalContentState: MutableStateFlow<ArticlesState> = 
        MutableStateFlow(ArticlesState(loading = true))
    
    val contentState: StateFlow<ArticlesState> = internalContentState
}
```

```swift
// iOS Wrapper
@MainActor
class ArticlesViewModelWrapper: ObservableObject {
    @Published private(set) var contentState: ArticlesState
    
    func startObservingChanges() async {
        contentStateTask = Task {
            for await contentState in articlesViewModel.contentState {
                self.contentState = contentState
            }
        }
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

### Core
- **Kotlin**: 2.0.20 / 2.1.20 (serialization)
- **Kotlinx Coroutines**: 1.7.3
- **Ktor**: 2.3.5
- **Koin**: 4.0.4
- **SQLDelight**: 2.0.2
- **SKIE**: 0.9.0

### Android
- **Jetpack Compose**: 1.5.4
- **Material Design 3**: 1.3.0
- **Navigation Compose**: 2.8.9
- **Coil**: 2.5.0

---

**Built with ❤️ using Kotlin Multiplatform** 