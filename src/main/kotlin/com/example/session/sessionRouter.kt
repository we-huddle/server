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
import org.jooq.DSLContext

fun Route.session(context: DSLContext) {
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