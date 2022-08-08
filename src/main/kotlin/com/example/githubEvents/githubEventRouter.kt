package com.example.githubEvents

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jooq.DSLContext

fun Route.githubEvents(context: DSLContext) {
    route("/github/event") {
        post {
            val eventType = EventType.valueOf(call.request.header("X-GitHub-Event")!!)
            when (eventType) {
                EventType.issues -> {
                    val issueEvent = call.receive<IssueEventPayload>()
                    handleIssueEventTrigger(issueEvent, context)
                }
                EventType.pull_request -> {
                    val pullEvent = call.receive<PullRequestEventPayload>()
                    handlePullRequestEventTrigger(pullEvent, context)
                }
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun handleIssueEventTrigger(issueEvent: IssueEventPayload, context: DSLContext) {
    // TODO: Write the code to handle issue events
}

fun handlePullRequestEventTrigger(pullRequestEvent: PullRequestEventPayload, context: DSLContext) {
    // TODO: Write the code to handle pr events
}
