plugins {
    `maven-publish`
}

tasks {
    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    create<Jar>("dokkaJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")

        from("$buildDir/dokka/html/") {
            include("**")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {

            artifact(tasks["dokkaJar"])
            artifact(tasks["sourcesJar"])

            repositories {
                pom {
                    name.set(rootProject.name)
                    artifactId = project.name
                    description.set("MiniGame library for Minecraft plugin")
//                    url.set("https://github.com/Netherald/Quantium/")
                    licenses {
                        license {
                            name.set("GNU General Public License Version 3")
                            url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("highright1234")
                            name.set("HighRight")
                        }
                    }
                }
            }
        }
    }
}