rootProject.name = "minipaper"
listOf("bukkit-api", "bukkit-core", "bungee-api", "bungee-core", "debug").map {
    "${rootProject.name}-$it"
}.forEach(::include)