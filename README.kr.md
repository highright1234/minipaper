# minipaper

페이퍼 플러그인용 미니게임 프레임워크

## 쓰는법
1. Gradle에 의존성 가져오기
* Gradle Kotlin DSL
```kotlin
repositories {
  maven("https://jitpack.io")
}

dependencies {
  compileOnly("com.github.highright1234.minipaper:minipaper-bukkit-api:VERSION")
}
```
2. 버킷에 Core 적용하기
