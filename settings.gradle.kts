rootProject.name = "redmc"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://repo.papermc.io/repository/maven-public/")
	}
}

dependencyResolutionManagement {
	repositories {
		mavenCentral()
		maven("https://repo.papermc.io/repository/maven-public/")
		maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	}
}

include(
	"plugins:test",
	"plugins:api",
	"plugins:permissions",
	"plugins:placeholders"
)