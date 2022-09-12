package com.example.routes.notifications

import com.example.plugins.UserPrinciple
import com.wehuddle.db.tables.Notification
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.jooq.DSLContext
import java.time.OffsetDateTime
import java.util.*

private val NOTIFICATION = Notification.NOTIFICATION

fun Route.notifications(context: DSLContext) {
    authenticate {
        route("/notifications") {
            authenticate {
                get{
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val notificationList =  context.fetch(NOTIFICATION.where(NOTIFICATION.PROFILEID.eq(userPrinciple.profileId).and(
                        NOTIFICATION.READ_STATUS.eq(false)))).toList()
                        .map { notificationRecord -> notificationRecord.toDto() }
                    call.respond(HttpStatusCode.OK, notificationList)
                }
                put{
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    context.update(NOTIFICATION)
                        .set(NOTIFICATION.READ_STATUS, true)
                        .set(NOTIFICATION.UPDATED_AT, OffsetDateTime.now())
                        .where(NOTIFICATION.PROFILEID.eq(userPrinciple.profileId).and(NOTIFICATION.READ_STATUS.eq(false)))
                        .execute()
                    call.respond(HttpStatusCode.OK)
                }
                put("/{notificationId}") {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val notificationId = UUID.fromString(call.parameters["notificationId"]!!)
                    context.update(NOTIFICATION)
                        .set(NOTIFICATION.READ_STATUS, true)
                        .set(NOTIFICATION.UPDATED_AT, OffsetDateTime.now())
                        .where(NOTIFICATION.PROFILEID.eq(userPrinciple.profileId).and(NOTIFICATION.ID.eq(notificationId)))
                        .execute()
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}

fun addNotification(notification: PartialNotificationDto, context: DSLContext){
    val newNotification = context.newRecord(NOTIFICATION)
    newNotification.profileid = notification.profileId
    newNotification.title = notification.title
    newNotification.description = notification.description
    newNotification.type  = notification.type
    newNotification.createdAt = OffsetDateTime.now()
    newNotification.updatedAt = OffsetDateTime.now()
    newNotification.store()
}