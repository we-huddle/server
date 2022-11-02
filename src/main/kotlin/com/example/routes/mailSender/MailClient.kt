package com.example.routes.mailSender

import java.util.concurrent.CompletableFuture

interface MailClient {
    fun sendEmail (emailDto: EmailDto): CompletableFuture<Void>
}