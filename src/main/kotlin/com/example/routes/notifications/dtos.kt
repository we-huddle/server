package com.example.routes.notifications

import java.util.UUID
import java.time.OffsetDateTime
import com.wehuddle.db.tables.records.NotificationRecord
import com.wehuddle.db.enums.NotificationType

open class PartialNotificationDto(
    open val profileId: UUID,
    open val linkId: UUID,
    open val title: String,
    open val description: String,
    open val type: NotificationType
)

class NotificationDto(
    val id: UUID,
    override val profileId: UUID,
    override val linkId: UUID,
    override val title: String,
    override val description: String,
    override val type: NotificationType,
    val read_status: Boolean,
    val created_at: OffsetDateTime,
    val updated_at: OffsetDateTime
) : PartialNotificationDto (
    profileId,
    linkId,
    title,
    description,
    type
)

fun NotificationRecord.toDto() = NotificationDto (
    this.id,
    this.profileid,
    this.linkid,
    this.title,
    this.description,
    this.type,
    this.readStatus,
    this.createdAt,
    this.updatedAt
)

