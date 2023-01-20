rootProject.name = "minipaper"
listOf(
    "common",
    "bukkit-api", "bukkit-core", "bukkit-debug",
    "bungee-api", "bungee-core", "bungee-debug",
    "publish"
).map {
    "${rootProject.name}-$it"
}.forEach(::include)
