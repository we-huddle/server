package com.example.plugins

import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowHost("localhost:3000", schemes = listOf("http", "https"))
        allowHost("elegant-jalebi-354786.netlify.app", schemes = listOf("http", "https"))
    }
}
