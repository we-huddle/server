package com.example.auth.github.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Url

data class UserGithubDto(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val name: String,
)

data class EmailGithubDto(
    val email: String,
    val primary: Boolean,
)

data class GetAccessTokenBodyDto(
    val client_id: String,
    val client_secret: String,
    val code: String,
)

interface GithubService {
    @POST
    fun getAccessToken(
        @Url url: String,
        @Body body: GetAccessTokenBodyDto,
    ): Call<String>

    @GET
    fun getUser(
        @Url url: String,
        @Header("Authorization") authHeader: String,
    ): Call<UserGithubDto>

    @GET
    fun getEmailsAddresses(
        @Url url: String,
        @Header("Authorization") authHeader: String,
    ): Call<List<EmailGithubDto>>
}