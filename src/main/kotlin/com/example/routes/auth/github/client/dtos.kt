package com.example.routes.auth.github.client

import com.example.plugins.toObject
import com.example.routes.tasks.DevTaskDetails
import com.example.routes.tasks.toDto
import com.wehuddle.db.enums.EventType
import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.Profile.PROFILE
import com.wehuddle.db.tables.records.FeedEventRecord
import com.wehuddle.db.tables.records.ProfileRecord
import java.time.OffsetDateTime
import java.util.UUID
import org.jooq.DSLContext

class ProfileLinks (
    val github: String,
    val linkedin: String,
    val stackoverflow: String,
    val twitter: String,
)

open class PartialProfileDto (
    open val bio: String?,
    open val city: String?,
    open val links: ProfileLinks?,
)

class ProfileDto(
    val id: UUID,
    val name: String,
    val githubUsername: String,
    val email: String,
    val photo: String,
    val githubUniqueId: String,
    val accessToken: String,
    val role: UserRole,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    override val bio: String?,
    override val city: String?,
    override val links: ProfileLinks?,
) : PartialProfileDto(
    bio,
    city,
    links
)

fun ProfileRecord.toDto(): ProfileDto {
    return ProfileDto(
        this.id,
        this.name,
        this.githubUsername,
        this.email,
        this.photo,
        this.githubUniqueId,
        this.accessToken,
        this.role,
        this.createdAt,
        this.updatedAt,
        this.bio,
        this.city,
        this.links?.data()?.toObject(ProfileLinks::class.java),
    )
}

class FeedEventDto(
    val id: UUID,
    val profile: ProfileDto,
    val title: String,
    val type: EventType,
    val referenceId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

fun FeedEventRecord.toDto(context: DSLContext): FeedEventDto {
    return FeedEventDto(
        id = this.id,
        profile = context.fetchOne(PROFILE.where(PROFILE.ID.eq(this.profileid)))!!.toDto(),
        title = this.title,
        type = this.type,
        referenceId = this.referenceid,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
