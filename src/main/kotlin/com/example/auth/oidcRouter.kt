package com.example.auth

import com.example.auth.github.client.GithubClient
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Profile
import com.wehuddle.db.tables.Session
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.get
import java.time.OffsetDateTime
import org.jooq.DSLContext

private val PROFILE = Profile.PROFILE
private val SESSION = Session.SESSION
private const val SESSION_VALIDITY_DAYS = 7L

fun Route.oidc(context: DSLContext, githubClient: GithubClient, clientUrl: String) {
    route("/authorize") {
        get {
            call.respondRedirect(githubClient.getAuthUrl())
        }
    }

    route("/callback") {
        get {
            val code = call.request.queryParameters["code"]!!
            val accessToken = githubClient.getAccessToken(code).accessToken
            val userInfo = githubClient.getUserInfo(accessToken)
            val emailAddressList = githubClient.getEmailAddresses(accessToken)
            var existingProfile = context.fetchOne(PROFILE.where(PROFILE.GITHUB_UNIQUE_ID.eq(userInfo.id.toString())))
            if (existingProfile == null) {
                val newProfile = context.newRecord(PROFILE)
                newProfile.name = userInfo.name
                newProfile.role = UserRole.HUDDLER
                newProfile.githubUsername = userInfo.login
                newProfile.githubUniqueId = userInfo.id.toString()
                newProfile.photo = userInfo.avatarUrl
                newProfile.accessToken = accessToken
                newProfile.email = emailAddressList.first { it.primary }.email
                newProfile.store()
                existingProfile = newProfile
            }
            val session = context.newRecord(SESSION)
            session.profileId = existingProfile.id
            session.expiresAt = OffsetDateTime.now().plusDays(SESSION_VALIDITY_DAYS)
            session.store()
            call.respondRedirect("$clientUrl/login/success?session=${session.id}")
        }
    }
}