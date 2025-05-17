
# RestClientKot24 – A Kotlin AAR Library for REST API Communication

**RestClientKot24** is a modern, extensible, and testable REST client library built in Kotlin using OkHttp. It wraps complex HTTP handling into a fluent, configurable API designed for Android and JVM-based apps.

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
- ✅ Full unit test suite with JUnit 4, Mockito, AndroidX

---

## 📦 Installation

1. Build the `.aar` using Gradle:

```bash
./gradlew :app:assembleRelease
```

2. Add the `.aar` to your consuming project:
```groovy
repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    implementation(name: 'restclientkot24-release', ext: 'aar')
}
```

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
- `RestClient`
- `Request`
- `Response`
- `Header`, `CacheData`
- `RestClientException`, `ConnectionException`
- `CacheManager` with `DatabaseHelper`

Run tests using:

```bash
./gradlew test
```

---

## 📂 Project Structure

```
📦 app/
├── java/
│   └── com.supunishara.restclientkot24/
│       ├── RestClient.kt
│       ├── Request.kt
│       ├── Response.kt
│       ├── cache/
│       ├── configs/
│       ├── callbacks/
│       ├── exceptions/
│       ├── data_classes/
│       └── helpers/
├── test/ (JUnit unit tests)
├── androidTest/ (Instrumented tests)
```

---

## 🛡️ License

MIT License. Feel free to fork and contribute.

---

## 🤝 Contributing

Pull requests are welcome! Please write tests for new features and ensure existing ones pass.

---

## 🔗 Author

**Supun Ishara**  
Email: [supun266@gmail.com](mailto:supun266@gmail.com)
