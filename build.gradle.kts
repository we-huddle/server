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
                        packageName = "no.wehuddle.db"
                        directory = databaseCodeGenerationDir
                    }
                    generate.isNullableAnnotation = true
                    generate.isNonnullAnnotation = true
                }
            }
        }
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.flywaydb:flyway-core:8.5.12")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.jooq:jooq:3.16.6")
    jooqGenerator("org.postgresql:postgresql:42.3.6")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    runtimeOnly("org.postgresql:postgresql:42.3.6")
}
