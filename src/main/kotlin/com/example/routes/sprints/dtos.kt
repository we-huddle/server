package com.example.routes.sprints

import com.example.plugins.gsonMapper
import com.example.plugins.toObject
import com.example.routes.githubEvents.GithubUser
import com.google.gson.reflect.TypeToken
import com.wehuddle.db.enums.IssueState
import com.wehuddle.db.tables.records.IssueRecord
import com.wehuddle.db.tables.records.SprintRecord
import java.time.OffsetDateTime
import java.util.UUID

open class PartialSprintDto(
    open val title: String,
    open val description: String,
    open val deadline: OffsetDateTime
)

class SprintDto(
    val id: UUID,
    val number: Int,
    title: String,
    description: String,
    deadline: OffsetDateTime,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
): PartialSprintDto(
    title,
    description,
    deadline,
)

fun SprintRecord.toDto() = SprintDto(
    this.id,
    this.number,
    this.title,
    this.description,
    this.deadline,
    this.createdAt,
    this.updatedAt,
)

class IssueDto(
    val id: UUID,
    val githubIssueId: Int,
    val title: String,
    val number: Int,
    val state: IssueState,
    val url: String,
    val githubUser: GithubUser,
    val assignees: List<GithubUser>,
    val repoName: String,
    val repoUrl: String,
    val openedAt: OffsetDateTime
)

fun IssueRecord.toDto() = IssueDto(
    this.id,
    this.githubIssueId,
    this.title,
    this.number,
    this.state,
    this.url,
    this.githubUser.data().toObject(GithubUser::class.java),
    gsonMapper.fromJson(this.assignees.data(), object: TypeToken<List<GithubUser>>(){}.type),
    this.repoName,
    this.repoUrl,
    this.openedAt
)
