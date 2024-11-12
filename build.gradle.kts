plugins {
    id("java")
    application
}

group = "com.hcnc"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.hcnc.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/net.dv8tion/JDA
    implementation("net.dv8tion:JDA:5.2.1")
}

tasks.test {
    useJUnitPlatform()
}