# shotokonoko
MiniGame framework for Paper plugins

## How to Import Api
1. Load api in gradle
* Gradle Kotlin DSL
```kotlin
repositories {
  maven("https://jitpack.io")
}

dependencies {
  compileOnly("com.github.highright1234.shotokonoko:shotokonoko-api:VERSION")
}
```
2. Apply plugin to bukkit
