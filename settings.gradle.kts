rootProject.name = "minipaper"
listOf("bukkit-api", "bukkit-core", "bungee-api", "bungee-core", "bukkit-debug").map {
    "${rootProject.name}-$it"
}.forEach(::include)