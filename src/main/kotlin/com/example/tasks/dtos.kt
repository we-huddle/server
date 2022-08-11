package com.example.tasks

import com.example.plugins.toJsonString
import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.tables.records.TaskRecord
import java.time.OffsetDateTime
import java.util.UUID

class TaskDto (
    val id: UUID,
    val title: String,
    val description: String,
    val type: TaskType,
    val details: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

fun TaskRecord.toDto() = TaskDto(
    this.id,
    this.title,
    this.description,
    this.type,
    this.details.toJsonString(),
    this.createdAt,
    this.updatedAt
)
