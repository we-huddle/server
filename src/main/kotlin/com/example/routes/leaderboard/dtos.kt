package com.example.routes.leaderboard

import com.example.routes.githubEvents.GithubUser

enum class LeaderboardPeriod {
    current, last, all
}

class LeaderboardRecord(
    val points: Int,
    val user: GithubUser
)
