package com.example.session

import com.example.plugins.UserPrinciple
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.time.OffsetDateTime

fun Route.session() {
    authenticate {
        route("/session/destroy") {
            get() {
                val principle = call.principal<UserPrinciple>()!!
                val session = principle.session
                session.expiresAt = OffsetDateTime.now()
                session.store()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
