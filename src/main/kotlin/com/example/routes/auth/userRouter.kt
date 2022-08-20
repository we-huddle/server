package com.example.routes.auth

import com.example.routes.auth.github.client.toDto
import com.example.plugins.UserPrinciple
import com.example.plugins.toJsonB
import com.example.routes.auth.github.client.PartialProfileDto
import com.wehuddle.db.tables.Profile
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.jooq.DSLContext
import java.util.*

private val PROFILE = Profile.PROFILE

fun Route.user(context: DSLContext) {
    route("/user") {
        authenticate {
            get("/self") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                call.respond(HttpStatusCode.OK, userPrinciple.profile.toDto())
            }
        }
    }
    route("/profile") {
        get("/{id}") {
            val profileId = UUID.fromString(call.parameters["id"]!!)
            val profile = context.fetchOne(PROFILE.where(PROFILE.ID.eq(profileId)))
            call.respond(HttpStatusCode.OK, profile!!.toDto())

        }
        authenticate {
            put{
                val userPrinciple = call.principal<UserPrinciple>()!!
                val profileDetailsToBeUpdated = call.receive<PartialProfileDto>()
                context.update(PROFILE)
                    .set(PROFILE.BIO, profileDetailsToBeUpdated.bio)
                    .set(PROFILE.CITY, profileDetailsToBeUpdated.city)
                    .set(PROFILE.LINKS, profileDetailsToBeUpdated.links?.toJsonB())
                    .where(PROFILE.ID.eq(userPrinciple.profileId))
                    .execute()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
