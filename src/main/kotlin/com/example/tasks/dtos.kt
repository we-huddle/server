package com.example.tasks

import com.example.plugins.toObject
import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.tables.records.TaskRecord
import java.time.OffsetDateTime
import java.util.UUID

class DevTaskDetails (
    val noOfPulls: Int,
)

open class PartialTaskDto (
    open val title: String,
    open val description: String,
    open val type: TaskType,
    open val details: DevTaskDetails,
)

class TaskDto (
    val id: UUID,
    override val title: String,
    override val description: String,
    override val type: TaskType,
    override val details: DevTaskDetails,
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
    this.details.data().toObject(DevTaskDetails::class.java),
    this.createdAt,
    this.updatedAt
)
