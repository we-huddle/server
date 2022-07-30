package com.example

import com.example.auth.github.client.GithubClient
import com.example.auth.github.client.GithubService
import com.example.plugins.configureHTTP
import com.example.plugins.configureHikariDataSource
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.performDBMigration
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

const val ENV_GITHUB_CLIENT_ID = "GITHUB_CLIENT_ID"
const val ENV_GITHUB_CLIENT_SECRET = "GITHUB_CLIENT_SECRET"
const val ENV_CLIENT_URL = "CLIENT_URL"

fun configureGithubClient(
    clientId: String = System.getenv(ENV_GITHUB_CLIENT_ID),
    clientSecret: String = System.getenv(ENV_GITHUB_CLIENT_SECRET),
): GithubClient {
    val githubService = Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create(ObjectMapper()))
        .baseUrl("https://hello.com")
        .build()
        .create(GithubService::class.java)
    return GithubClient(clientId, clientSecret, githubService)
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        val datasource = configureHikariDataSource()
        val context = DSL.using(datasource, SQLDialect.POSTGRES)
        val clientUrl = System.getenv(ENV_CLIENT_URL)
        performDBMigration(datasource)
        configureSecurity(context)
        configureHTTP()
        configureRouting(context, configureGithubClient(), clientUrl)

        install(ContentNegotiation) {
            val mapper = ObjectMapper()
            mapper.findAndRegisterModules()
            mapper.registerModule(JavaTimeModule())
            register(ContentType.Application.Json, JacksonConverter(mapper))
        }
    }.start(wait = true)
}
