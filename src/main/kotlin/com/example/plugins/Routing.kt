package com.example.plugins

import com.example.routes.auth.github.client.GithubClient
import com.example.routes.auth.oidc
import com.example.routes.auth.user
import com.example.routes.badges.badge
import com.example.routes.dataBucket.dataBucket
import com.example.routes.githubEvents.githubEvents
import com.example.routes.leaderboard.leaderboard
import com.example.routes.session.session
import com.example.routes.sprints.sprints
import com.example.routes.tasks.tasks
import com.example.routes.notifications.notifications
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.request.httpMethod
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import org.jooq.DSLContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val ERR_TYP_BASE = "https://wehuddle.org/errors"

open class ErrorResource(
    val typePostFix: String,
    val title: String,
    val status: HttpStatusCode,
    val detail: String,
    val instance: String
) {
    val type: String = "$ERR_TYP_BASE/$typePostFix"

    @Suppress("LongMethod")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ErrorResource) return false

        if (typePostFix != other.typePostFix) return false
        if (title != other.title) return false
        if (status != other.status) return false
        if (detail != other.detail) return false
        if (instance != other.instance) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = typePostFix.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + detail.hashCode()
        result = 31 * result + instance.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

class RestException(val errorResource: ErrorResource) : Exception(errorResource.title)

fun Application.configureRouting(
    context: DSLContext? = null,
    githubClient: GithubClient? = null,
    clientUrl: String? = null,
    dataBucketName: String? = null
) {
    routing {
        oidc(context!!, githubClient!!, clientUrl!!)
        session()
        user(context)
        githubEvents(context)
        tasks(context)
        sprints(context)
        leaderboard(context)
        badge(context)
        dataBucket(dataBucketName!!)
        notifications(context)
    }
    intercept(ApplicationCallPipeline.Monitoring) {
        interceptExceptions(this, LoggerFactory.getLogger(Application::class.java))
    }
}

suspend fun interceptExceptions(pipelineContext: PipelineContext<Unit, ApplicationCall>, logger: Logger) {
    try {
        val method = pipelineContext.call.request.httpMethod.value
        val uri = pipelineContext.call.request.uri
        logger.info("$method $uri")
        pipelineContext.proceed()
    } catch (err: RestException) {
        logger.error(err.message, err)
        pipelineContext.call.respond(err.errorResource.status, err.errorResource)
    } catch (err: Throwable) {
        logger.error(err.message, err)
        val message: String = err.localizedMessage ?: err.message ?: "Unexpected error: ${err.javaClass.name}"
        pipelineContext.call.respond(HttpStatusCode.InternalServerError, message)
    } finally {
        logger.info("responding with ${pipelineContext.call.response.status()}")
    }
}
