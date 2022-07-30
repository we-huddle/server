package com.example.plugins

import com.example.auth.github.client.GithubClient
import com.example.auth.oidc
import com.example.session.session
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.jooq.DSLContext

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
    clientUrl: String? = null
) {
    routing {
        oidc(context!!, githubClient!!, clientUrl!!)
        session(context)
    }
}
