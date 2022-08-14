package com.example.tasks

import com.example.plugins.UserPrinciple
import com.example.plugins.toJsonB
import com.example.plugins.toJsonString
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Task
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jooq.DSLContext

private val TASK = Task.TASK

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

            post {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                    val taskToBeCreated = call.receive<PartialTaskDto>()
                    val newTaskRecord = context.newRecord(TASK)
                    newTaskRecord.title = taskToBeCreated.title
                    newTaskRecord.description = taskToBeCreated.description
                    newTaskRecord.type = taskToBeCreated.type
                    newTaskRecord.details = taskToBeCreated.details.toJsonB()
                    newTaskRecord.store()
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Permission denied")
                }

            }
        }


    }
}