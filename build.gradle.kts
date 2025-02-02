plugins {
    java
    application // Include if your project has a main class
}

group = "" // Replace with your package name (e.g., "com.example")
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral() // Pull dependencies from Maven Central
}

dependencies {
    // MySQL Connector/J (for database connectivity)
    implementation("mysql:mysql-connector-java:8.0.33") // Use version 8.0.33 (matches your .iml)

    // JCalendar (date picker library)
    implementation("com.toedter:jcalendar:1.4") // From Maven Central

    // Argon2 (password hashing)
    implementation("de.mkammerer:argon2-jvm:2.4")

    // jBCrypt (alternative password hashing)
    implementation("de.svenkubiak:jBCrypt:0.4.3")

    // Testing framework
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass.set("LoginGUI") // Replace with your main class (e.g., "com.example.Main")
}

tasks.test {
    useJUnitPlatform() // Use JUnit 5
}