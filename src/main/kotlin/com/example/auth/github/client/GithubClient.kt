package com.example.auth.github.client

class GithubClient(
    private val clientId: String,
    private val clientSecret: String,
    private val githubService: GithubService
) {
    fun getAuthUrl(): String {
        val authUrl = StringBuilder("https://github.com/login/oauth/authorize?scope=user:email")
        authUrl.append("client_id=")
        authUrl.append(clientId)
        return authUrl.toString()
    }

    // TODO: Implement a exception handler for rest calls
    fun getAccessToken(code: String): String {
        val payload = GetAccessTokenBodyDto(
            clientId,
            clientSecret,
            code
        )
        return githubService.getAccessToken(
            url = "https://github.com/login/oauth/access_token",
            body = payload
        ).execute().let {
            if (it.isSuccessful) {
                it.body() ?: throw Exception()
            } else {
                throw Exception()
            }
        }
    }

    fun getUserInfo(accessToken: String): UserGithubDto {
        return githubService.getUser(
            url = "https://api.github.com/user",
            authHeader = "token $accessToken"
        ).execute().let {
            if (it.isSuccessful) {
                it.body() ?: throw Exception()
            } else {
                throw Exception()
            }
        }
    }

    fun getEmailAddresses(accessToken: String): List<EmailGithubDto> {
        return githubService.getEmailsAddresses(
            url = "https://api.github.com/user/emails",
            authHeader = "token $accessToken"
        ).execute().let {
            if (it.isSuccessful) {
                it.body() ?: throw Exception()
            } else {
                throw Exception()
            }
        }
    }
}