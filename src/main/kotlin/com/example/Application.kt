package com.example

import com.example.auth.github.client.GithubClient
import com.example.auth.github.client.GithubService
import com.example.plugins.configureHTTP
import com.example.plugins.configureHikariDataSource
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.performDBMigration
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import retrofit2.Retrofit

const val ENV_GITHUB_CLIENT_ID = "GITHUB_CLIENT_ID"
const val ENV_GITHUB_CLIENT_SECRET = "GITHUB_CLIENT_SECRET"

fun configureGithubClient(
    clientId: String = System.getenv(ENV_GITHUB_CLIENT_ID),
    clientSecret: String = System.getenv(ENV_GITHUB_CLIENT_SECRET),
): GithubClient {
    val githubService = Retrofit.Builder().baseUrl("").build().create(GithubService::class.java)
    return GithubClient(clientId, clientSecret, githubService)
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        val datasource = configureHikariDataSource()
        val context = DSL.using(datasource, SQLDialect.POSTGRES)
        performDBMigration(datasource)
        configureSecurity()
        configureHTTP()
        configureRouting(context, configureGithubClient())
    }.start(wait = true)
}
