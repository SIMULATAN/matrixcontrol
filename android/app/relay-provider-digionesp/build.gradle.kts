plugins {
	id("matrixcontrol.relay-provider")
}

dependencies {
	implementation(projects.app.relayProviderTcp)
	// service discovery client provided by digi.com
	// from ftp://ftp1.digi.com/support/utilities/AddpClient.zip
	implementation(files("libs/AddpClient.jar"))
}
