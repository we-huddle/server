package com.example.tasks

import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.tables.records.TaskRecord
import java.time.OffsetDateTime
import java.util.UUID

open class PartialTaskDto (
    open val title: String,
    open val description: String,
    open val type: TaskType,
    open val details: String,
)

class TaskDto (
    val id: UUID,
    override val title: String,
    override val description: String,
    override val type: TaskType,
    override val details: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
) : PartialTaskDto (
    title,
    description,
    type,
    details
)

fun TaskRecord.toDto() = TaskDto(
    this.id,
    this.title,
    this.description,
    this.type,
    this.details.data(),
    this.createdAt,
    this.updatedAt
)
