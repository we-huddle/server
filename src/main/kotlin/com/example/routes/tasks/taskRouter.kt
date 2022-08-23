package com.example.routes.tasks

import com.example.plugins.UserPrinciple
import com.example.plugins.toJsonB
import com.example.routes.auth.github.client.toDto
import com.wehuddle.db.enums.AnswerStatus
import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Answer
import com.wehuddle.db.tables.Profile
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
private val PROFILE = Profile.PROFILE

fun Route.tasks(context: DSLContext) {
    authenticate {
        route("/tasks") {
            get {
                val taskList = context.fetch(TASK).map { taskRecord ->
                    when (taskRecord.type) {
                        TaskType.QUIZ -> taskRecord.toDto<PartialQuizTaskDetails>()
                        TaskType.DEV -> taskRecord.toDto<DevTaskDetails>()
                        else -> throw Exception()
                    }
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
                    val response = when (existingTask.type) {
                        TaskType.DEV -> existingTask.toDto<DevTaskDetails>()
                        TaskType.QUIZ -> existingTask.toDto<PartialQuizTaskDetails>()
                        else -> throw Exception()
                    }
                    call.respond(HttpStatusCode.OK, response)
                }


                get("/completed/users") {
                    val taskId = UUID.fromString(call.parameters["taskId"]!!)
                    val existingTask = context.fetchOne(
                        TASK.where(TASK.ID.eq(taskId))
                    )
                    if (existingTask == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                        return@get
                    }
                    val profileList = context.fetch(PROFILE.join(ANSWER).on(
                        PROFILE.ID.eq(ANSWER.PROFILEID)
                            .and(ANSWER.TASKID.eq(taskId))
                            .and(ANSWER.STATUS.eq(AnswerStatus.COMPLETED))
                    )).into(PROFILE)
                        .toList()
                        .map { profileRecord -> profileRecord.toDto() }
                    call.respond(HttpStatusCode.OK, profileList)
                }

                put ("/{type}"){
                    val taskId = UUID.fromString(call.parameters["taskId"]!!)
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        val taskToBeUpdated = when (call.parameters["type"]) {
                            "dev" -> call.receive<PartialTaskDto<DevTaskDetails>>()
                            "quiz" -> call.receive<PartialTaskDto<QuizTaskDetails>>()
                            else -> throw Exception()
                        }
                        context.update(TASK)
                            .set(TASK.TITLE, taskToBeUpdated.title)
                            .set(TASK.DESCRIPTION, taskToBeUpdated.description)
                            .set(TASK.DETAILS, taskToBeUpdated.details.toJsonB())
                            .set(TASK.UPDATED_AT, OffsetDateTime.now())
                            .where(TASK.ID.eq(taskId))
                            .execute()
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
                }

                get("/agent") {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        val taskId = UUID.fromString(call.parameters["taskId"]!!)
                        val existingTask = context.fetchOne(
                            TASK.where(TASK.ID.eq(taskId))
                        )
                        if (existingTask == null) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                            return@get
                        }
                        val response = when (existingTask.type) {
                            TaskType.DEV -> existingTask.toDto<DevTaskDetails>()
                            TaskType.QUIZ -> existingTask.toDto<QuizTaskDetails>()
                            else -> throw Exception()
                        }
                        call.respond(HttpStatusCode.OK, response)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
                }

                route("/answer") {
                    post {
                        val profile = call.principal<UserPrinciple>()!!
                        val answerPayload = call.receive<QuizAnswerPayload>()
                        val task = context.fetchOne(TASK.where(TASK.ID.eq(answerPayload.taskId)))?.toDto<QuizTaskDetails>()
                        if (task == null || task.type == TaskType.DEV) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                            return@post
                        }
                        val answers = answerPayload.answers
                        var count = 0
                        for (question in task.details.questions) {
                            val givenAnswer = answers[question.number]
                            if (givenAnswer == question.correctAnswerKey) count++
                        }
                        val score = (count.toDouble()/task.details.questions.size)*100
                        val newAnswer = context.newRecord(ANSWER)
                        newAnswer.taskid = task.id
                        newAnswer.status = if (score >= task.details.passMark)
                            AnswerStatus.COMPLETED else AnswerStatus.INCOMPLETE
                        newAnswer.details = answerPayload.toJsonB()
                        newAnswer.profileid = profile.profileId
                        newAnswer.createdAt = OffsetDateTime.now()
                        newAnswer.updatedAt = OffsetDateTime.now()
                        newAnswer.store()
                        call.respond(HttpStatusCode.OK)
                    }

                    get {
                        val profile = call.principal<UserPrinciple>()!!
                        val taskId = UUID.fromString(call.parameters["taskId"]!!)
                        val existingTask = context.fetchOne(
                            TASK.where(TASK.ID.eq(taskId))
                        )
                        if (existingTask == null) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid task id")
                            return@get
                        }
                        val answerList = context.fetch(
                            ANSWER.where(ANSWER.TASKID.eq(taskId).and(ANSWER.PROFILEID.eq(profile.profileId)))
                        ).toList().map { answerRecord ->
                            when (existingTask.type) {
                                TaskType.DEV -> answerRecord.toDto<DevTaskDetails>()
                                TaskType.QUIZ -> answerRecord.toDto<QuizAnswerPayload>()
                                else -> throw Exception()
                            }
                        }
                        call.respond(HttpStatusCode.OK, answerList)
                    }
                }
            }

            post("/create/{type}") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                    val taskToBeCreated = when (call.parameters["type"]) {
                        "dev" -> call.receive<PartialTaskDto<DevTaskDetails>>()
                        "quiz" -> call.receive<PartialTaskDto<QuizTaskDetails>>()
                        else -> throw Exception()
                    }
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
                    val taskList = context
                        .select()
                        .from(TASK)
                        .join(ANSWER)
                        .on(TASK.ID.eq(ANSWER.TASKID))
                        .where(ANSWER.PROFILEID.eq(userPrinciple.profileId))
                        .and(ANSWER.STATUS.eq(AnswerStatus.COMPLETED))
                        .fetch()
                        .into(Task.TASK)
                        .map { taskRecord ->
                            when (taskRecord.type) {
                                TaskType.QUIZ -> taskRecord.toDto<QuizTaskDetails>()
                                TaskType.DEV -> taskRecord.toDto<DevTaskDetails>()
                                else -> throw Exception()
                            }
                        }
                    call.respond(HttpStatusCode.OK, taskList)
                }
            }
        }
    }
}
