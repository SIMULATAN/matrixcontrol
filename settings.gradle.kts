rootProject.name = "matrixcontrol"

dependencyResolutionManagement {
	repositories {
		mavenCentral()
	}
}

include("protocol")
include("relay")
include("cli")
include("relay-server")
