package com.example.routes.pullRequests

import com.wehuddle.db.tables.PullRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jooq.DSLContext
import java.util.*

private val PULL_REQUEST = PullRequest.PULL_REQUEST

fun Route.pullRequests(context: DSLContext){
        route("/pullRequests/merged/byUser/{profileId}"){
            get {
                val profileId = UUID.fromString(call.parameters["profileId"]!!)
                val prList = context
                    .select(PULL_REQUEST.asterisk())
                    .from (PULL_REQUEST)
                    .where(PULL_REQUEST.PROFILE_ID.eq(profileId))
                    .and (PULL_REQUEST.MERGED)
                    .orderBy(PULL_REQUEST.OPENED_AT.desc())
                    .fetchInto(PULL_REQUEST)
                    .toList()
                    .map{PullRequestRecord -> PullRequestRecord.toDto()}

                call.respond(HttpStatusCode.OK, prList)
            }
        }

}
