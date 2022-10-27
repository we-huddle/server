package com.example.routes.auth

import com.example.routes.auth.github.client.toDto
import com.example.plugins.UserPrinciple
import com.example.plugins.toJsonB
import com.example.routes.auth.github.client.PartialProfileDto
import com.wehuddle.db.enums.EventType
import com.wehuddle.db.tables.FeedEvent.FEED_EVENT
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Profile
import com.wehuddle.db.tables.UserFollower.USER_FOLLOWER
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.time.OffsetDateTime
import org.jooq.DSLContext
import java.util.*

private val PROFILE = Profile.PROFILE

fun Route.user(context: DSLContext) {
    route("/user") {
        authenticate {
            get("/self") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                call.respond(HttpStatusCode.OK, userPrinciple.profile.toDto())
            }
            get("/feed-events") {
                val userPrinciple = call.principal<UserPrinciple>()!!
                val followerIdList = context.fetch(
                    USER_FOLLOWER
                        .join(PROFILE)
                        .on(PROFILE.ID.eq(USER_FOLLOWER.FOLLOWS))
                        .where(USER_FOLLOWER.PROFILEID.eq(userPrinciple.profileId))
                ).into(PROFILE).toList().map { it.id }
                val feedEvents = context
                    .fetch(FEED_EVENT.where(FEED_EVENT.PROFILEID.`in`(followerIdList)))
                    .into(FEED_EVENT)
                    .toList()
                    .map { it.toDto(context) }
                    .sortedByDescending { it.createdAt }
                call.respond(HttpStatusCode.OK, feedEvents)
            }

            route("/{id}") {
                post("/follow") {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val profileId = UUID.fromString(call.parameters["id"]!!)
                    val toFollowProfile = context.fetchOne(PROFILE.where(PROFILE.ID.eq(profileId)))
                    if (toFollowProfile == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid profile id")
                        return@post
                    }
                    val existingFollowRecord = context.fetchOne(
                        USER_FOLLOWER.where(
                            USER_FOLLOWER.PROFILEID.eq(userPrinciple.profileId)
                                .and(USER_FOLLOWER.FOLLOWS.eq(toFollowProfile.id))
                        )
                    )
                    if (existingFollowRecord != null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid follower id")
                        return@post
                    }
                    val followerRecord = context.newRecord(USER_FOLLOWER)
                    followerRecord.profileid = userPrinciple.profileId
                    followerRecord.follows = toFollowProfile.id
                    followerRecord.createdAt = OffsetDateTime.now()
                    followerRecord.updatedAt = OffsetDateTime.now()
                    followerRecord.store()
                    addFeedEvent(
                        context = context,
                        profileId = profileId,
                        title = "${userPrinciple.profile.name} has started following ${toFollowProfile.name}",
                        type = EventType.FOLLOWER,
                        referenceId = toFollowProfile.id
                    )
                    call.respond(HttpStatusCode.OK)
                }

                post("/unfollow") {
                    val userPrinciple = call.principal<UserPrinciple>()!!
                    val followedProfileId = UUID.fromString(call.parameters["id"]!!)
                    val existingFollowRecord = context.fetchOne(
                        USER_FOLLOWER.where(
                            USER_FOLLOWER.PROFILEID.eq(userPrinciple.profileId)
                                .and(USER_FOLLOWER.FOLLOWS.eq(followedProfileId))
                        )
                    )
                    if (existingFollowRecord == null) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid follower id")
                        return@post
                    }
                    existingFollowRecord.delete()

                }
            }
            get("/allUsers") {
                val userList = context.fetch(PROFILE).toList().map { ProfileRecord -> ProfileRecord.toDto() }
                call.respond(HttpStatusCode.OK, userList)
            }
            put("updateRole/revokeAgent/{id}"){
                val profileId = UUID.fromString(call.parameters["id"]!!)
                context.update(PROFILE)
                    .set(PROFILE.ROLE, UserRole.HUDDLER)
                    .where(PROFILE.ID.eq(profileId))
                    .execute()
                call.respond(HttpStatusCode.OK)
            }
            put("updateRole/grantAgent/{id}"){
                val profileId = UUID.fromString(call.parameters["id"]!!)
                context.update(PROFILE)
                    .set(PROFILE.ROLE, UserRole.HUDDLE_AGENT)
                    .where(PROFILE.ID.eq(profileId))
                    .execute()
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    route("/profile") {
        get("/{id}") {
            val profileId = UUID.fromString(call.parameters["id"]!!)
            val profile = context.fetchOne(PROFILE.where(PROFILE.ID.eq(profileId)))
            call.respond(HttpStatusCode.OK, profile!!.toDto())
        }

        get("/{id}/followers") {
            val profileId = UUID.fromString(call.parameters["id"]!!)
            val profile = context.fetchOne(PROFILE.where(PROFILE.ID.eq(profileId)))
            if (profile == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid profile id")
                return@get
            }
            val followerList = context.fetch(
                USER_FOLLOWER
                    .join(PROFILE)
                    .on(PROFILE.ID.eq(USER_FOLLOWER.PROFILEID))
                    .where(USER_FOLLOWER.FOLLOWS.eq(profileId))
            ).into(PROFILE).toList().map { it.toDto() }
            call.respond(HttpStatusCode.OK, followerList)
        }

        authenticate {
            put{
                val userPrinciple = call.principal<UserPrinciple>()!!
                val profileDetailsToBeUpdated = call.receive<PartialProfileDto>()
                context.update(PROFILE)
                    .set(PROFILE.BIO, profileDetailsToBeUpdated.bio)
                    .set(PROFILE.CITY, profileDetailsToBeUpdated.city)
                    .set(PROFILE.LINKS, profileDetailsToBeUpdated.links?.toJsonB())
                    .where(PROFILE.ID.eq(userPrinciple.profileId))
                    .execute()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun addFeedEvent(context: DSLContext, profileId: UUID, title: String, type: EventType, referenceId: UUID) {
    val event = context.newRecord(FEED_EVENT)
    event.profileid = profileId
    event.title = title
    event.type = type
    event.referenceid = referenceId
    event.createdAt = OffsetDateTime.now()
    event.updatedAt = OffsetDateTime.now()
    event.store()
}
