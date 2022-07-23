package com.example

import com.example.plugins.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        val datasource = configureHikariDataSource()
        performDBMigration(datasource)
        configureSecurity()
        configureHTTP()
        configureRouting(datasource)
    }.start(wait = true)
}
