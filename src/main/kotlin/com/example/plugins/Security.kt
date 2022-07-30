package com.example.plugins

import com.example.auth.SESSION_VALIDITY_DAYS
import com.wehuddle.db.tables.Profile
import com.wehuddle.db.tables.Session
import com.wehuddle.db.tables.records.ProfileRecord
import com.wehuddle.db.tables.records.SessionRecord
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.*
import io.ktor.server.application.*
import java.time.OffsetDateTime
import java.util.*
import org.jooq.DSLContext

object BearerAuthError {
    val invalidTokenException = {
        RestException(
            ErrorResource(
                "/auth/token/invalid",
                "Invalid token",
                HttpStatusCode.Unauthorized,
                "Token must be a valid UUID",
                ""
            )
        )
    }
    val invalidAuthHeaderTypeException = {
        RestException(
            ErrorResource(
                "/auth/header/type/invalid",
                "Invalid auth header type",
                HttpStatusCode.Unauthorized,
                "Auth header type Single expected",
                ""
            )
        )
    }
    val invalidAuthSchemeException = {
        RestException(
            ErrorResource(
                "/auth/scheme/type/invalid",
                "Invalid auth scheme type",
                HttpStatusCode.Unauthorized,
                "Auth scheme type Bearer expected",
                ""
            )
        )
    }
}

data class TokenCredential(val token: UUID) : Credential

class BearerAuthenticationProvider internal constructor(
    configuration: Configuration
) : AuthenticationProvider(configuration) {
    internal val authenticationFunction = configuration.authenticationFunction

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val authHeader = context.call.request.parseAuthorizationHeader()
        val authScheme = authHeader?.authScheme?.lowercase()
        val token = when {
            (authHeader !is HttpAuthHeader.Single) -> throw BearerAuthError.invalidAuthHeaderTypeException()
            authScheme != "bearer" -> throw BearerAuthError.invalidAuthSchemeException()
            else -> try {
                UUID.fromString(authHeader.blob)
            } catch (e: IllegalArgumentException) {
                throw BearerAuthError.invalidTokenException()
            }
        }

        val credentials = TokenCredential(token)
        val principal = authenticationFunction(context.call, credentials)
        principal ?: throw BearerAuthError.invalidTokenException()
        context.principal(principal)
    }

    class Configuration internal constructor(name: String?) : Config(name) {
        internal var authenticationFunction: AuthenticationFunction<TokenCredential> = {
            throw NotImplementedError(
                "Bearer auth validate function is not specified. Use bearer { validate { ... } } to fix."
            )
        }

        fun validate(body: suspend ApplicationCall.(TokenCredential) -> Principal?) {
            authenticationFunction = body
        }
    }
}

fun AuthenticationConfig.bearer(
    name: String? = null,
    configure: BearerAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = BearerAuthenticationProvider(BearerAuthenticationProvider.Configuration(name).apply(configure))
    register(provider)
}

class UserPrinciple(
    val profileId: UUID,
    val session: SessionRecord,
    val profile: ProfileRecord
) : Principal

fun Application.configureSecurity(dslContext: DSLContext) {
    install(Authentication) {
        bearer {
            validate { tokenCredential ->
                val row = dslContext.select(Profile.PROFILE.asterisk(), Session.SESSION.asterisk())
                    .from(Profile.PROFILE)
                    .leftJoin(Session.SESSION)
                    .on(Session.SESSION.PROFILE_ID.eq(Profile.PROFILE.ID))
                    .where(Session.SESSION.ID.eq(tokenCredential.token))
                    .fetchOne() ?: return@validate null
                val profile = row.into(Profile.PROFILE)
                val session = row.into(Session.SESSION)
                if (session.expiresAt.isBefore(OffsetDateTime.now())) {
                    return@validate null
                }
                session.expiresAt = OffsetDateTime.now().plusDays(SESSION_VALIDITY_DAYS)
                session.store()
                return@validate UserPrinciple(profile.id, session, profile)
            }
        }
    }
}
