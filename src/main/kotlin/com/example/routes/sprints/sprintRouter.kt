package com.example.routes.sprints

import aws.smithy.kotlin.runtime.http.response.dumpResponse
import com.example.plugins.UserPrinciple
import com.example.routes.mailSender.EmailDto
import com.example.routes.mailSender.SmtpMailClient
import com.example.routes.tasks.DevTaskDetails
import com.example.routes.tasks.QuizAnswerPayload
import com.example.routes.tasks.toDto
import com.wehuddle.db.enums.IssueState
import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.time.OffsetDateTime
import java.util.UUID
import org.jooq.DSLContext

private val SPRINT = Sprint.SPRINT
private val ISSUE = Issue.ISSUE
private val PROFILE = Profile.PROFILE
private val ISSUE_ASSIGNMENT = IssueAssignment.ISSUE_ASSIGNMENT
private val SPRINT_ISSUE = SprintIssue.SPRINT_ISSUE

fun Route.sprints(context: DSLContext, mailClient: SmtpMailClient) {
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
                route("/send-reminder") {
                    post {
                        val userPrinciple = call.principal<UserPrinciple>()!!
                        if (userPrinciple.profile.role != UserRole.HUDDLE_AGENT) {
                            call.respond(HttpStatusCode.Forbidden)
                            return@post
                        }

                        val sprint = call.receive<SprintDto>()
                        val emailAddresses = context
                            .select(PROFILE.EMAIL)
                            .from(PROFILE)
                            .join(ISSUE_ASSIGNMENT)
                            .on(PROFILE.ID.eq(ISSUE_ASSIGNMENT.PROFILE_ID))
                            .join(SPRINT_ISSUE)
                            .on(SPRINT_ISSUE.SPRINT_ID.eq(sprint.id))
                            .fetch(PROFILE.EMAIL)
                            .toList()
                        println(emailAddresses)
                        emailAddresses.forEach(){ email ->
                            val emailToSend = EmailDto(sprint.title, email, sprint.description, OffsetDateTime.now())
                            mailClient.sendEmail(emailToSend)
                        }
                    }
                }

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

                put {
                    val sprintId = UUID.fromString(call.parameters["sprintId"]!!)
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        val sprintToBeUpdated = call.receive<PartialSprintDto>()
                        context.update(SPRINT)
                            .set(SPRINT.TITLE, sprintToBeUpdated.title)
                            .set(SPRINT.DESCRIPTION, sprintToBeUpdated.description)
                            .set(SPRINT.DEADLINE, sprintToBeUpdated.deadline)
                            .set(SPRINT.UPDATED_AT, OffsetDateTime.now())
                            .where(SPRINT.ID.eq(sprintId))
                            .execute()
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
                }

                delete {
                    val sprintId = UUID.fromString(call.parameters["sprintId"]!!)
                    val existingSprint = context.fetchOne(
                        SPRINT.where(SPRINT.ID.eq(sprintId))
                    )
                    if (existingSprint == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid sprint id")
                    }
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if(userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        context.deleteFrom(SPRINT).where(SPRINT.ID.eq(sprintId)).execute()
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
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
