rootProject.name = "minipaper"
listOf("api", "core", "debug", "bungee", "bungee-api").map {
    "${rootProject.name}-$it"
}.forEach(::include)