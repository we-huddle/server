package com.example.githubEvents

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime

enum class EventType {
    issues, pull_request
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class IssueEventPayload(
    val issue: GithubIssue,
    val repository: GithubRepository,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PullRequestEventPayload(
    val pull_request: GithubPullRequest,
    val repository: GithubRepository,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubIssue(
    val id: Int,
    val title: String,
    val number: Int,
    val html_url: String,
    val state: String,
    val body: String?,
    val user: GithubUser,
    val assignees: List<GithubUser>,
    val created_at: OffsetDateTime,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubRepository(
    val full_name: String,
    val html_url: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubPullRequest(
    val id: Int,
    val title: String,
    val number: Int,
    val state: String,
    val html_url: String,
    val merged: Boolean,
    val user: GithubUser,
    val assignees: List<GithubUser>,
    val created_at: OffsetDateTime,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GithubUser(
    val id: Int,
    val login: String,
    val html_url: String,
)
