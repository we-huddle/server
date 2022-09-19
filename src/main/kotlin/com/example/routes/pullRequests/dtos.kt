package com.example.routes.pullRequests

import com.wehuddle.db.tables.PullRequest
import com.wehuddle.db.tables.records.PullRequestRecord
import java.time.OffsetDateTime
import java.util.*

class PullRequestDto(
    val id: UUID,
    val github_pr_id: Int,
    val profile_id: UUID,
    val title: String,
    val number: Int,
    val url: String,
    val repo_name: String,
    val repo_url: String,
    val openedAt: OffsetDateTime
)

fun PullRequestRecord.toDto() = PullRequestDto(
    this.id,
    this.githubPrId,
    this.profileId,
    this.title,
    this.number,
    this.url,
    this.repoName,
    this.repoUrl,
    this.openedAt,
)
