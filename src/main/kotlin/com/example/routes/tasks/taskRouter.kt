package com.example.routes.tasks

import com.example.plugins.UserPrinciple
import com.example.plugins.toJsonB
import com.wehuddle.db.enums.AnswerStatus
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Answer
import com.wehuddle.db.tables.Task
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.delete
import io.ktor.server.routing.route
import java.time.OffsetDateTime
import org.jooq.DSLContext
import java.util.*

private val TASK = Task.TASK
private val ANSWER = Answer.ANSWER

fun Route.tasks(context: DSLContext) {
    authenticate {
        route("/tasks") {
            get {
                val taskListResults = context.fetch(TASK)
                val taskList = mutableListOf<TaskDto>()
                for (record in taskListResults) {
                    taskList.add(record.toDto())
                }
                call.respond(HttpStatusCode.OK, taskList)
            }

            route("/{taskId}") {
                get {
                    val taskId = UUID.fromString(call.parameters["taskId"]!!)
                    val existingTask = context.fetchOne(
                        TASK.where(TASK.ID.eq(taskId))
                    )
                    if (existingTask == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                        return@get
                    }
                    call.respond(HttpStatusCode.OK, existingTask.toDto())
                }
            }

            post {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                    val taskToBeCreated = call.receive<PartialTaskDto>()
                    val newTaskRecord = context.newRecord(TASK)
                    newTaskRecord.title = taskToBeCreated.title
                    newTaskRecord.description = taskToBeCreated.description
                    newTaskRecord.type = taskToBeCreated.type
                    newTaskRecord.details = taskToBeCreated.details.toJsonB()
                    newTaskRecord.createdAt = OffsetDateTime.now()
                    newTaskRecord.updatedAt = OffsetDateTime.now()
                    newTaskRecord.store()
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Permission denied")
                }

            }

            put {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                    val taskToBeUpdated = call.receive<TaskDto>()
                    context.update(TASK)
                        .set(TASK.TITLE, taskToBeUpdated.title)
                        .set(TASK.DESCRIPTION, taskToBeUpdated.description)
                        .set(TASK.DETAILS, taskToBeUpdated.details.toJsonB())
                        .where(TASK.ID.eq(taskToBeUpdated.id))
                        .execute()
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Permission denied")
                }
            }

            delete("/{id}") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                    val taskIDToBeDeleted = UUID.fromString(call.parameters["id"]!!)
                    context.deleteFrom(TASK).where(TASK.ID.eq(taskIDToBeDeleted)).execute()
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Permission denied")
                }
            }

            route("/completed") {
                get {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val result = context
                        .select()
                        .from(TASK)
                        .join(ANSWER)
                        .on(TASK.ID.eq(ANSWER.TASKID))
                        .where(ANSWER.PROFILEID.eq(userPrinciple.profileId))
                        .and(ANSWER.STATUS.eq(AnswerStatus.COMPLETED))
                        .fetch()
                        .into(Task.TASK)
                    val taskList = mutableListOf<TaskDto>()
                    for (record in result) {
                        taskList.add(record.toDto())
                    }
                    call.respond(HttpStatusCode.OK, taskList)
                }
            }
        }
    }
}
