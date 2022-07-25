package com.example.plugins

import com.example.auth.github.client.GithubClient
import com.example.auth.oidc
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.jooq.DSLContext

fun Application.configureRouting(
    context: DSLContext? = null,
    githubClient: GithubClient? = null,
    clientUrl: String? = null
) {
    routing {
        oidc(context!!, githubClient!!, clientUrl!!)
    }
}
