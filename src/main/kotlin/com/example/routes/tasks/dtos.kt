package com.example.routes.tasks

import com.example.plugins.toObject
import com.wehuddle.db.enums.AnswerStatus
import com.wehuddle.db.enums.TaskType
import com.wehuddle.db.tables.records.AnswerRecord
import com.wehuddle.db.tables.records.TaskRecord
import java.time.OffsetDateTime
import java.util.UUID

class DevTaskDetails(
    val noOfPulls: Int,
)

open class PartialQuestion(
    val number: Int,
    val question: String,
    val options: Map<String, String>,
)

class Question(
    number: Int,
    question: String,
    val correctAnswerKey: String,
    options: Map<String, String>,
): PartialQuestion(
    number,
    question,
    options,
)

class PartialQuizTaskDetails(
    val passMark: Double,
    val questions: List<PartialQuestion>,
)

class QuizTaskDetails(
    val passMark: Double,
    val questions: List<Question>,
)

class QuizAnswerPayload(
    val taskId: UUID,
    val answers: Map<Int, String>,
)

open class PartialTaskDto<T>(
    open val title: String,
    open val description: String,
    open val type: TaskType,
    open val details: T,
)

class TaskDto<T>(
    val id: UUID,
    override val title: String,
    override val description: String,
    override val type: TaskType,
    override val details: T,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
) : PartialTaskDto<T>(
    title,
    description,
    type,
    details
)

inline fun <reified T> TaskRecord.toDto() = TaskDto(
    this.id,
    this.title,
    this.description,
    this.type,
    this.details.data().toObject(T::class.java),
    this.createdAt,
    this.updatedAt
)

class AnswerDto<T>(
    val id: UUID,
    val profileId: UUID,
    val taskId: UUID,
    val details: T,
    val status: AnswerStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

inline fun <reified T> AnswerRecord.toDto() = AnswerDto(
    this.id,
    this.profileid,
    this.taskid,
    this.details.data().toObject(T::class.java),
    this.status,
    this.createdAt,
    this.updatedAt,
)
