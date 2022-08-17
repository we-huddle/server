package com.example.routes.auth.github.client

import com.wehuddle.db.enums.UserRole
import com.wehuddle.db.tables.records.ProfileRecord
import java.time.OffsetDateTime
import java.util.UUID

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
        this.updatedAt
    )
}
