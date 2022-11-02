package com.example.routes.mailSender

import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import java.util.concurrent.CompletableFuture


class SmtpMailClient(
    private val host: String,
    private val port: Int,
    private val username: String,
    private val password: String,
) : MailClient {

    private val mailer: Mailer = MailerBuilder.withSMTPServer(host, port, username, password).buildMailer()

    override fun sendEmail(emailDto: EmailDto): CompletableFuture<Void> {
        val email = EmailBuilder.startingBlank()
            .from(username)
            .to(emailDto.emailAddresses)
            .withSubject(emailDto.subject)
            .withHTMLText(emailDto.htmlBody)
            .buildEmail()
        return mailer.sendMail(email)
    }
}
