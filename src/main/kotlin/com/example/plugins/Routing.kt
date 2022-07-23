package com.example.plugins

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(datasource: HikariDataSource? = null) {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "Hello World!")
        }
    }
}
