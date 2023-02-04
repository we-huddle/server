package com.example.routes.mailSender

import java.time.OffsetDateTime


open class PartialEmailDto(
    open val subject: String,
    open val emailAddresses:  String,
    open val htmlBody: String
)

class EmailDto(
    subject: String,
    emailAddresses: String,
    htmlBody: String,
    val createdAt: OffsetDateTime,
): PartialEmailDto(
    subject,
    emailAddresses,
    htmlBody
)
