package com.example

import com.example.plugins.configureHTTP
import com.example.plugins.configureHikariDataSource
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureHTTP()
        configureRouting(configureHikariDataSource())
    }.start(wait = true)
}
