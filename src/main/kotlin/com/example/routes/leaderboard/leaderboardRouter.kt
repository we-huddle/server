package com.example.routes.leaderboard

import com.example.plugins.toObject
import com.example.routes.githubEvents.GithubUser
import com.wehuddle.db.tables.PullRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.time.OffsetDateTime
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.impl.DSL

private val PULL_REQUEST = PullRequest.PULL_REQUEST

fun Route.leaderboard(context: DSLContext) {
    route("/leaderboard/{timePeriod}") {
        get {
            val now = OffsetDateTime.now()
            val (startDate: OffsetDateTime, endDate: OffsetDateTime) = when(LeaderboardPeriod.valueOf(call.parameters["timePeriod"]!!)) {
                LeaderboardPeriod.current -> {
                    listOf(
                        now.withDayOfMonth(1)
                            .withHour(0).withMinute(0).withSecond(0),
                        now
                    )
                }
                LeaderboardPeriod.last -> {
                    listOf(
                        now.minusMonths(1)
                            .withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0),
                        now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0),
                    )
                }
                LeaderboardPeriod.all -> {
                    listOf(OffsetDateTime.now().withYear(1970), now)
                }
            }
            val fetchedRecords = context
                .select(DSL.jsonbObjectAgg(PULL_REQUEST.GITHUB_USER), DSL.count())
                .from(PULL_REQUEST)
                .where(PULL_REQUEST.OPENED_AT.between(startDate).and(endDate))
                .and(PULL_REQUEST.MERGED)
                .groupBy(PULL_REQUEST.GITHUB_USER)
                .fetch()
            val leaderboardRecords = mutableListOf<LeaderboardRecord>();
            for (record in fetchedRecords) {
                val githubUser = (record.get(0) as JSONB).data().toObject(GithubUser::class.java)
                val points = (record.get(1) as Int) * 10
                leaderboardRecords.add(LeaderboardRecord(points, githubUser))
            }
            leaderboardRecords.sortByDescending { leaderboardRecord -> leaderboardRecord.points }
            call.respond(HttpStatusCode.OK, leaderboardRecords)
        }
    }
}