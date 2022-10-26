package com.example.routes.badges

import com.example.plugins.UserPrinciple
import com.example.routes.auth.addFeedEvent
import com.example.routes.notifications.PartialNotificationDto
import com.example.routes.notifications.addNotification
import com.example.routes.sprints.SPRINT
import com.wehuddle.db.enums.AnswerStatus
import com.wehuddle.db.enums.EventType
import com.wehuddle.db.enums.NotificationType
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Answer
import com.wehuddle.db.tables.Badge
import com.wehuddle.db.tables.BadgeAchievement
import com.wehuddle.db.tables.Task
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import java.time.OffsetDateTime
import java.util.*
import org.jooq.DSLContext
import org.jooq.impl.DSL

private val BADGE = Badge.BADGE
private val TASK = Task.TASK
private val BADGE_ACHIEVEMENT = BadgeAchievement.BADGE_ACHIEVEMENT
private val ANSWER = Answer.ANSWER

fun Route.badge(context: DSLContext) {
    route("/badges") {
        authenticate {
            post {
                val userPrinciple = call.principal<UserPrinciple>()!!
                if (userPrinciple.profile.role != UserRole.HUDDLE_AGENT) {
                    call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    return@post
                }
                val badgeToBeCreated = call.receive<PartialBadgeDto>()
                for (taskId in badgeToBeCreated.depTasks) {
                    if (context.fetchOne(TASK.where(TASK.ID.eq(taskId))) == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "Task with $taskId doesn't exist in the system"
                        )
                        return@post
                    }
                }
                for (badgeId in badgeToBeCreated.depBadges) {
                    if (context.fetchOne(BADGE.where(BADGE.ID.eq(badgeId))) == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "Badge with $badgeId doesn't exist in the system"
                        )
                        return@post
                    }
                }
                val badge = context.newRecord(BADGE)
                badge.title = badgeToBeCreated.title
                badge.description = badgeToBeCreated.description
                badge.photo = badgeToBeCreated.photo
                badge.depBadges = badgeToBeCreated.depBadges.toTypedArray()
                badge.depTasks = badgeToBeCreated.depTasks.toTypedArray()
                badge.createdAt = OffsetDateTime.now()
                badge.updatedAt = OffsetDateTime.now()
                badge.store()
                call.respond(HttpStatusCode.OK, badge.toDto())
            }

            route("/{badgeId}") {
                get {
                    val badgeId = UUID.fromString(call.parameters["badgeId"]!!)
                    val badge = context.fetchOne(BADGE.where(BADGE.ID.eq(badgeId)))
                    if (badge == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid badge id")
                        return@get
                    }
                    call.respond(HttpStatusCode.OK, badge.toBadgeWithDependenciesDto(context))
                }

                put {
                    val badgeId = UUID.fromString(call.parameters["badgeId"]!!)
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if (userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        val badgeToBeUpdated = call.receive<EditBadgeDTO>()
                        context.update(BADGE)
                            .set(BADGE.TITLE, badgeToBeUpdated.title)
                            .set(BADGE.DESCRIPTION, badgeToBeUpdated.description)
                            .set(BADGE.PHOTO, badgeToBeUpdated.photo)
                            .set(BADGE.UPDATED_AT, OffsetDateTime.now())
                            .where(BADGE.ID.eq(badgeId))
                            .execute()
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
                }

                delete {
                    val badgeId = UUID.fromString(call.parameters["badgeId"]!!)
                    val existingBadge = context.fetchOne(
                        BADGE.where(BADGE.ID.eq(badgeId))
                    )
                    if (existingBadge == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid badge id")
                    }
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    if(userPrinciple.profile.role == UserRole.HUDDLE_AGENT) {
                        context.deleteFrom(BADGE).where(BADGE.ID.eq(badgeId)).execute()
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden, "Permission denied")
                    }
                }
            }

            route("/completed") {
                get {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val badgeList = context
                        .select()
                        .from(BADGE)
                        .join(BADGE_ACHIEVEMENT)
                        .on(BADGE.ID.eq(BADGE_ACHIEVEMENT.BADGEID))
                        .where(BADGE_ACHIEVEMENT.PROFILEID.eq(userPrinciple.profileId))
                        .fetch()
                        .into(BADGE)
                        .map { badgeRecord -> badgeRecord.toDto() }
                    call.respond(HttpStatusCode.OK, badgeList)
                }
            }


        }

        get {
            val badgeList = context.fetch(BADGE).toList().map { badgeRecord -> badgeRecord.toDto() }
            call.respond(HttpStatusCode.OK, badgeList)
        }
        route("/completedByUser/{profileId}") {
            get {
                val profileId = UUID.fromString(call.parameters["profileId"]!!)
                val badgeList = context
                    .select()
                    .from(BADGE)
                    .join(BADGE_ACHIEVEMENT)
                    .on(BADGE.ID.eq(BADGE_ACHIEVEMENT.BADGEID))
                    .where(BADGE_ACHIEVEMENT.PROFILEID.eq(profileId))
                    .fetch()
                    .into(BADGE)
                    .map { badgeRecord -> badgeRecord.toDto() }
                call.respond(HttpStatusCode.OK, badgeList)
            }
        }
    }
}

object BadgeFunctions {
    fun findAndGrantBadges(context: DSLContext, profileId: UUID) {
        context.transaction { config ->
            val transactionContext = DSL.using(config)
            var grantedBadgeCount = grantBadges(transactionContext, profileId)
            while (grantedBadgeCount != 0) {
                grantedBadgeCount = grantBadges(transactionContext, profileId)
            }
        }
    }

    private fun grantBadges(context: DSLContext, profileId: UUID): Int {
        val alreadyOwnedBadgeIdList = context.fetch(
            BADGE.join(BADGE_ACHIEVEMENT).on(BADGE_ACHIEVEMENT.BADGEID.eq(BADGE.ID))
                .and(BADGE_ACHIEVEMENT.PROFILEID.eq(profileId))
        ).into(BADGE).toList().map { badgeRecord -> badgeRecord.id }.toMutableList()
        val completedTaskIdList = context.fetch(
            TASK.join(ANSWER).on(TASK.ID.eq(ANSWER.TASKID))
                .and(ANSWER.PROFILEID.eq(profileId))
                .and(ANSWER.STATUS.eq(AnswerStatus.COMPLETED))
        ).into(TASK).toList().map { taskRecord -> taskRecord.id }
        val potentialBadgesList = context.fetch(
            BADGE.where(BADGE.ID.notIn(alreadyOwnedBadgeIdList))
        ).into(BADGE).toList().map { badgeRecord -> badgeRecord.toDto() }
        var grantedBadgeCount = 0
        for (badge in potentialBadgesList) {
            val isBadgeGrantable = alreadyOwnedBadgeIdList.containsAll(badge.depBadges)
                    && completedTaskIdList.containsAll(badge.depTasks)
                    && !alreadyOwnedBadgeIdList.contains(badge.id)
            if (isBadgeGrantable) {
                grantedBadgeCount++
                alreadyOwnedBadgeIdList.add(badge.id)
                val badgeAchievement = context.newRecord(BADGE_ACHIEVEMENT)
                badgeAchievement.profileid = profileId
                badgeAchievement.badgeid = badge.id
                badgeAchievement.createdAt = OffsetDateTime.now()
                badgeAchievement.updatedAt = OffsetDateTime.now()
                badgeAchievement.store()

                val notification = PartialNotificationDto(profileId, badge.id, "${badge.title} achieved", "Congratulations on achieving ${badge.title}!" , NotificationType.BADGE )
                addNotification(notification, context)
                addFeedEvent(
                    context = context,
                    profileId = profileId,
                    title = "New badge achieved, ${badge.title}",
                    type = EventType.BADGE,
                    referenceId = badge.id
                )
            }
        }
        return grantedBadgeCount
    }
}
