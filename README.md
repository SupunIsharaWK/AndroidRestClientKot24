# RestClientKot24 – A Kotlin AAR Library for REST API Communication

**RestClientKot24** is a modern, extensible, and testable REST client library built in Kotlin using OkHttp. It wraps complex HTTP handling into a fluent, configurable API designed for Android and JVM-based apps.

> 🔖 **Version 1.0.2** – Initial public release published via GitHub Packages.

---

## 🚀 Features

- ✅ Fluent `Request` object builder
- ✅ Supports GET, POST, PUT, PATCH, DELETE
- ✅ Configurable headers, body (JSON, form, multipart, etc.)
- ✅ Built-in coroutine support (`suspend fun executeRequestSuspend()`)
- ✅ Multiple callback support:
  - `MultiResponseCallback` (success, failure, retry, cache)
  - `ProgressCallbacks` (start/end)
- ✅ Persistent caching using SQLite via `CacheManager`
- ✅ Support for Gzip decoding
- ✅ Request debugging: log headers, payloads, response
- ✅ SSL & HostnameVerifier injection
- ✅ Optional in-memory cookie jar support
- ✅ Full unit test suite with JUnit 4, MockK, and AndroidX

---

## 📦 Installation

You can integrate **RestClientKot24** using GitHub Packages.

### Step 1: Add GitHub Maven repository

In your project-level `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/SupunIsharaWK/AndroidRestClientKot24")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
```
### Step 2: Add dependency
In your app-level build.gradle.kts:

```bash
dependencies {
    implementation("com.github.SupunIsharaWK:restclientkot24:1.0.2")
}
```
💡 Don’t forget to define gpr.user and gpr.key in your ~/.gradle/gradle.properties.
---

## 🔧 Basic Usage

```kotlin
val request = Request.fromUrl("https://api.example.com", Request.Method.POST).apply {
  setBody(JSONObject().put("name", "TestBody"))
  addHeader(Header("Authorization", "Bearer token"))
  enableResponseCaching(60000)
}

val restClient = RestClient(context)
restClient.setConfig(ConfigBuilder().debugPrintInfo().build())

restClient.executeRequest(request,
  multiCallback = object : MultiResponseCallback() {
    override fun onSuccess(response: Response) {
      Log.d("API", response.responseBody)
    }
    override fun onFailure(exception: Throwable) {
      Log.e("API", "Failed", exception)
    }
  }
)

```

---

## 🧪 Unit Testing
The project includes comprehensive unit tests for:

*   RestClient
*   Request
*   Response
*   Header, CacheData
*   RestClientException, ConnectionException
*   CacheManager with DatabaseHelper

Run tests using:
```bash
./gradlew test

```

---

## 📂 Project Structure

```
📦 restclientkot24/
├── java/com/supunishara/restclientkot24/
│   ├── RestClient.kt
│   ├── Request.kt
│   ├── Response.kt
│   ├── cache/
│   ├── configs/
│   ├── callbacks/
│   ├── exceptions/
│   ├── data_classes/
│   └── helpers/
├── test/                # Unit tests
├── androidTest/         # Instrumented tests
```

---

## 🛡🛡️ License

MIT License. Feel free to fork and contribute.

---

## 🤝 Contributing

Pull requests are welcome! Please write unit tests for any new features and ensure all existing tests pass.


---

## 👤 Author

**Supun Weerasekara**
📧 Email: supun266@gmail.com
🔗 GitHub: SupunIsharaWK
