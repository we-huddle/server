val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

val databaseCodeGenerationDir = "$rootDir/generated/src/main/java"

plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("nu.studer.jooq") version "7.1.1"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

jooq {
    version.set("3.14.0") // the default (can be omitted)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS) // the default (can be omitted)
    configurations {
        create("java") { // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(false) // default (can be omitted)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/wehuddle"
                    user = "wehuddle"
                    password = "secret"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    target.apply {
                        packageName = "com.wehuddle.db"
                        directory = databaseCodeGenerationDir
                    }
                    generate.isNullableAnnotation = false
                    generate.isNonnullAnnotation = false
                }
            }
        }
    }
}

sourceSets["main"].java {
    srcDir(databaseCodeGenerationDir)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("org.flywaydb:flyway-core:8.5.12")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.jooq:jooq:3.16.6")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.2")
    implementation("io.ktor:ktor-serialization-jackson:2.0.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.7.3")
    jooqGenerator("org.postgresql:postgresql:42.3.6")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    runtimeOnly("org.postgresql:postgresql:42.3.6")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("aws.sdk.kotlin:s3:0.17.5-beta")
}
