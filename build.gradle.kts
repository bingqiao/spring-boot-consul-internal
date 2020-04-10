import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.freefair.lombok") version "4.1.0"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.cloud:spring-cloud-starter-consul-all:2.2.2.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-config:2.2.2.RELEASE")

    implementation("commons-io:commons-io:2.6")
    implementation("commons-codec:commons-codec:1.10")

    implementation("org.apache.httpcomponents:httpclient:4.5.3")
    implementation("org.apache.commons:commons-lang3:3.9")


    // needed for java 11
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.2")
    implementation("javax.activation:activation:1.1.1")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

description = "Oddle Consul Internal"
version = "1.0.0-SNAPSHOT"
group = "com.oodlefinance.bing.qiao"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

tasks.register<Copy>("unpack") {
    dependsOn(":bootJar")
    from(zipTree(tasks.getByName<BootJar>("bootJar").outputs.files.singleFile))
    into("build/dependency")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}
