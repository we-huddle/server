package com.example.routes.dataBucket

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.ObjectCannedAcl
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.dataBucket(bucketName: String) {
    authenticate {
        route("/image") {
            post {
                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { partData ->
                    when (partData) {
                        is PartData.FileItem -> {
                            val fileName = (partData.originalFileName as String).replace(' ', '-')
                            val fileBytes = partData.streamProvider().readBytes()
                            val metadataVal = mutableMapOf<String, String>()
                            metadataVal["type"] = partData.contentType.toString()
                            val request = PutObjectRequest {
                                bucket = bucketName
                                key = fileName
                                metadata = metadataVal
                                body = ByteStream.fromBytes(fileBytes)
                                acl = ObjectCannedAcl.PublicRead
                            }
                            S3Client{ region = "ap-south-1" }.use { s3 ->
                                s3.putObject(request)
                            }
                            call.respond(
                                HttpStatusCode.OK,
                                "https://${bucketName}.s3.ap-south-1.amazonaws.com/${fileName}"
                            )
                        }
                        else -> {
                            call.respond(HttpStatusCode.BadRequest)
                        }
                    }
                }
            }
        }
    }
}
