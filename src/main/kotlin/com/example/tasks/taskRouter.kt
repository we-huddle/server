package com.example.tasks

import com.wehuddle.db.tables.Task
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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
        }
    }
}