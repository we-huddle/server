package com.example.routes.auth

import com.example.routes.auth.github.client.toDto
import com.example.plugins.UserPrinciple
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jooq.DSLContext

fun Route.user(context: DSLContext) {
    route("/user") {
        authenticate {
            get("/self") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                call.respond(HttpStatusCode.OK, userPrinciple.profile.toDto())
            }
        }
    }
}
