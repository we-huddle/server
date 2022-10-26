package com.example.routes.badges

import com.example.routes.tasks.TaskDto
import com.example.routes.tasks.toDto
import com.wehuddle.db.tables.Badge
import com.wehuddle.db.tables.Task
import com.wehuddle.db.tables.records.BadgeRecord
import java.time.OffsetDateTime
import java.util.UUID
import org.jooq.DSLContext

private val BADGE = Badge.BADGE
private val TASK = Task.TASK

open class EditBadgeDTO(
    val title: String,
    val description: String,
    val photo: String
)

open class PartialBadgeDto(
    title: String,
    description: String,
    photo: String,
    val depBadges: List<UUID>,
    val depTasks: List<UUID>,
): EditBadgeDTO(
    title,
    description,
    photo
)

open class BadgeDto(
    val id: UUID,
    title: String,
    description: String,
    photo: String,
    depBadges: List<UUID>,
    depTasks: List<UUID>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
): PartialBadgeDto(
    title,
    description,
    photo,
    depBadges,
    depTasks
)

class BadgeWithDependenciesDto(
    val id: UUID,
    val title: String,
    val description: String,
    val photo: String,
    val depBadges: List<BadgeDto>,
    val depTasks: List<TaskDto<Any>>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

fun BadgeRecord.toDto() = BadgeDto(
    this.id,
    this.title,
    this.description,
    this.photo,
    this.depBadges.toList(),
    this.depTasks.toList(),
    this.createdAt,
    this.updatedAt,
)

fun BadgeRecord.toBadgeWithDependenciesDto(context: DSLContext) = BadgeWithDependenciesDto(
    this.id,
    this.title,
    this.description,
    this.photo,
    this.depBadges.map { badgeId -> context.fetchOne(BADGE.where(BADGE.ID.eq(badgeId)))!!.toDto() },
    this.depTasks.map { taskId -> context.fetchOne(TASK.where(TASK.ID.eq(taskId)))!!.toDto() },
    this.createdAt,
    this.updatedAt,
)
