package com.example.routes.auth.github.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Url

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserGithubDto(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("avatar_url")
    val avatarUrl: String,
    @JsonProperty("name")
    val name: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EmailGithubDto(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("primary")
    val primary: Boolean,
)

data class GetAccessTokenBodyDto(
    val client_id: String,
    val client_secret: String,
    val code: String,
)

data class AccessTokenPayload(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("scope")
    val scope: String,
)

interface GithubService {
    @POST
    fun getAccessToken(
        @Url url: String,
        @Header("Accept") acceptHeader: String,
        @Body body: GetAccessTokenBodyDto,
    ): Call<AccessTokenPayload>

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