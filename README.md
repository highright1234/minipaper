# minipaper

[한국어](https://github.com/highright1234/minipaper/blob/master/README.kr.md)

MiniGame framework for Paper plugins

## How to Import Api
1. Load api in gradle
* Gradle Kotlin DSL
```kotlin
repositories {
  maven("https://jitpack.io")
}

dependencies {
  compileOnly("com.github.highright1234.minipaper:minipaper-bukkit-api:VERSION")
}
```
2. Apply plugin to bukkit
