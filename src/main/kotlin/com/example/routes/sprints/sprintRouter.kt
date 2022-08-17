package com.example.routes.sprints

import com.example.plugins.UserPrinciple
import com.wehuddle.db.enums.IssueState
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Issue
import com.wehuddle.db.tables.Sprint
import com.wehuddle.db.tables.SprintIssue
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.time.OffsetDateTime
import java.util.UUID
import org.jooq.DSLContext

private val SPRINT = Sprint.SPRINT
private val ISSUE = Issue.ISSUE

fun Route.sprints(context: DSLContext) {
    authenticate {
        route("/sprints") {
            post {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role != UserRole.HUDDLE_AGENT) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
                val sprintToBeCreated = call.receive<PartialSprintDto>()
                if (sprintToBeCreated.deadline <= OffsetDateTime.now()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid deadline")
                    return@post
                }
                val newSprint = context.newRecord(SPRINT)
                newSprint.title = sprintToBeCreated.title
                newSprint.description = sprintToBeCreated.description
                newSprint.deadline = sprintToBeCreated.deadline
                newSprint.createdAt = OffsetDateTime.now()
                newSprint.updatedAt = OffsetDateTime.now()
                newSprint.store()
                call.respond(HttpStatusCode.OK, newSprint.toDto())
            }

            get {
                val sprintList = context.fetch(SPRINT).toList().map {
                        sprintRecord -> sprintRecord.toDto()
                }.sortedByDescending {
                        sprint -> sprint.deadline
                }
                call.respond(HttpStatusCode.OK, sprintList)
            }

            route("/{sprintId}") {
                get {
                    val sprintId = UUID.fromString(call.parameters["sprintId"]!!)
                    val existingSprint = context.fetchOne(
                        SPRINT.where(SPRINT.ID.eq(sprintId))
                    )
                    if (existingSprint == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid sprint id")
                        return@get
                    }
                    call.respond(HttpStatusCode.OK, existingSprint.toDto())
                }

                route("/issues") {
                    post {
                        val userPrinciple = call.principal<UserPrinciple>()!!
                        if (userPrinciple.profile.role != UserRole.HUDDLE_AGENT) {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }
                        val listOfIssueIdsToAdd = call.receive<List<UUID>>()
                        val sprintId = UUID.fromString(call.parameters["sprintId"]!!)
                        val existingSprint = context.fetchOne(
                            SPRINT.where(SPRINT.ID.eq(sprintId))
                        )
                        if (existingSprint == null) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid sprint id")
                        }
                        for (issueId in listOfIssueIdsToAdd) {
                            val newSprintIssue = context.newRecord(SprintIssue.SPRINT_ISSUE)
                            newSprintIssue.sprintId = sprintId
                            newSprintIssue.issueId = issueId
                            newSprintIssue.store()
                        }
                        call.respond(HttpStatusCode.OK)
                    }

                    get {
                        val sprintId = UUID.fromString(call.parameters["sprintId"]!!)
                        val existingSprint = context.fetchOne(
                            SPRINT.where(SPRINT.ID.eq(sprintId))
                        )
                        if (existingSprint == null) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid sprint id")
                        }
                        val issueList = context
                            .select(ISSUE.asterisk())
                            .from(ISSUE)
                            .join(SprintIssue.SPRINT_ISSUE)
                            .on(ISSUE.ID.eq(SprintIssue.SPRINT_ISSUE.ISSUE_ID))
                            .where(SprintIssue.SPRINT_ISSUE.SPRINT_ID.eq(sprintId))
                            .fetchInto(ISSUE)
                            .toList()
                            .map { issueRecord -> issueRecord.toDto() }
                        call.respond(HttpStatusCode.OK, issueList)
                    }
                }
            }
        }

        route("/issues") {
            get {
                val issueList = context.fetch(ISSUE).toList().map {
                    issueRecord -> issueRecord.toDto()
                }
                call.respond(HttpStatusCode.OK, issueList)
            }

            get("/open") {
                val issueList = context
                    .fetch(ISSUE.where(ISSUE.STATE.eq(IssueState.open))).toList().map {
                        issueRecord -> issueRecord.toDto()
                    }
                call.respond(HttpStatusCode.OK, issueList)
            }
        }
    }
}
