package com.example.routes.githubEvents

import com.example.plugins.toJsonB
import com.example.routes.badges.BadgeFunctions
import com.example.routes.notifications.PartialNotificationDto
import com.example.routes.notifications.addNotification
import com.example.routes.tasks.DevTaskDetails
import com.example.routes.tasks.toDto
import com.wehuddle.db.enums.*
import com.wehuddle.db.tables.Answer
import com.wehuddle.db.tables.Issue
import com.wehuddle.db.tables.IssueAssignment
import com.wehuddle.db.tables.Profile
import com.wehuddle.db.tables.PullRequest
import com.wehuddle.db.tables.Task
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.time.OffsetDateTime
import org.jooq.DSLContext
import org.jooq.impl.DSL

private val PULL_REQUEST = PullRequest.PULL_REQUEST
private val ISSUE = Issue.ISSUE
private val ISSUE_ASSIGNMENT = IssueAssignment.ISSUE_ASSIGNMENT
private val PROFILE = Profile.PROFILE
private val TASK = Task.TASK
private val ANSWER = Answer.ANSWER

fun Route.githubEvents(context: DSLContext) {
    route("/github/event") {
        post {
            val eventType = EventType.valueOf(call.request.header("X-GitHub-Event")!!)
            when (eventType) {
                EventType.issues -> {
                    val issueEvent = call.receive<IssueEventPayload>()
                    handleIssueEventTrigger(issueEvent, context)
                }
                EventType.pull_request -> {
                    val pullEvent = call.receive<PullRequestEventPayload>()
                    handlePullRequestEventTrigger(pullEvent, context)
                }
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun handleIssueEventTrigger(issueEvent: IssueEventPayload, context: DSLContext) {
    val (issue, repository) = issueEvent
    val issueExists: Boolean = context.transactionResult { config ->
        val transactionContext = DSL.using(config)
        val existingIssue = transactionContext.fetchOne(
            ISSUE.where(ISSUE.GITHUB_ISSUE_ID.eq(issue.id))
        ) ?: return@transactionResult false
        existingIssue.title = issue.title
        existingIssue.number = issue.number
        existingIssue.state = IssueState.valueOf(issue.state)
        existingIssue.url = issue.html_url
        existingIssue.githubUser = issue.user.toJsonB()
        existingIssue.assignees = issue.assignees.toJsonB()
        existingIssue.repoName = repository.full_name
        existingIssue.repoUrl = repository.html_url
        existingIssue.openedAt = issue.created_at
        existingIssue.store()

        transactionContext.deleteFrom(ISSUE_ASSIGNMENT)
            .where(ISSUE_ASSIGNMENT.ISSUE_ID.eq(existingIssue.id))
            .execute()

        for (assignee in issue.assignees) {
            val existingProfile = transactionContext.fetchOne(
                PROFILE.where(PROFILE.GITHUB_UNIQUE_ID.eq(assignee.id.toString()))
            )
            if (existingProfile != null) {
                val newIssueAssignment = transactionContext.newRecord(ISSUE_ASSIGNMENT)
                newIssueAssignment.issueId = existingIssue.id
                newIssueAssignment.profileId = existingProfile.id
                newIssueAssignment.store()
            }
        }

        return@transactionResult true
    }
    if (issueExists) return
    context.transaction { config ->
        val transactionContext = DSL.using(config)

        val newIssue = transactionContext.newRecord(ISSUE)
        newIssue.githubIssueId = issue.id
        newIssue.title = issue.title
        newIssue.number = issue.number
        newIssue.state = IssueState.valueOf(issue.state)
        newIssue.url = issue.html_url
        newIssue.githubUser = issue.user.toJsonB()
        newIssue.assignees = issue.assignees.toJsonB()
        newIssue.repoName = repository.full_name
        newIssue.repoUrl = repository.html_url
        newIssue.openedAt = issue.created_at
        newIssue.store()

        for (assignee in issue.assignees) {
            val existingProfile = transactionContext.fetchOne(
                PROFILE.where(PROFILE.GITHUB_UNIQUE_ID.eq(assignee.id.toString()))
            )
            if (existingProfile != null) {
                val newIssueAssignment = transactionContext.newRecord(ISSUE_ASSIGNMENT)
                newIssueAssignment.issueId = newIssue.id
                newIssueAssignment.profileId = existingProfile.id
                newIssueAssignment.store()
            }
        }
    }
}

fun handlePullRequestEventTrigger(pullRequestEvent: PullRequestEventPayload, context: DSLContext) {
    val (pull_request, repository) = pullRequestEvent
    val isExistingPR = context.transactionResult { config ->
        val transactionContext = DSL.using(config)
        val existingPullRequest = transactionContext.fetchOne(
            PULL_REQUEST.where(PULL_REQUEST.GITHUB_PR_ID.eq(pull_request.id))
        ) ?: return@transactionResult false
        val associatedProfileId = transactionContext.fetchOne(
            PROFILE.where(PROFILE.GITHUB_UNIQUE_ID.eq(pull_request.user.id.toString()))
        )?.id
        if (!existingPullRequest.merged && pull_request.merged && associatedProfileId != null) {
            val prCount = transactionContext
                .fetchCount(
                    PULL_REQUEST.where(PULL_REQUEST.PROFILE_ID.eq(associatedProfileId).and(PULL_REQUEST.MERGED))
                ) + 1
            val incompleteDevTasks = transactionContext
                .select(TASK.asterisk())
                .from(TASK)
                .leftJoin(ANSWER)
                .on(
                    TASK.ID.eq(ANSWER.TASKID)
                        .and(ANSWER.PROFILEID.eq(associatedProfileId))
                        .and(ANSWER.STATUS.eq(AnswerStatus.COMPLETED))
                )
                .where(TASK.TYPE.eq(TaskType.DEV))
                .and(ANSWER.ID.isNull)
                .fetchInto(TASK)
                .toList()
                .map { taskRecord -> taskRecord.toDto<DevTaskDetails>() }
            for (task in incompleteDevTasks) {
                if (task.details.noOfPulls <= prCount) {
                    val notification = PartialNotificationDto(associatedProfileId, task.id, "${task.title} completed", "Congratulations on completing ${task.title}!" , NotificationType.TASK )
                    addNotification(notification, context)

                    val newAnswer = transactionContext.newRecord(ANSWER)
                    newAnswer.profileid = associatedProfileId
                    newAnswer.taskid = task.id
                    newAnswer.status = AnswerStatus.COMPLETED
                    newAnswer.details = task.details.toJsonB()
                    newAnswer.createdAt = OffsetDateTime.now()
                    newAnswer.updatedAt = OffsetDateTime.now()
                    newAnswer.store()
                }
            }
            BadgeFunctions.findAndGrantBadges(context, associatedProfileId)
        }
        existingPullRequest.githubPrId = pull_request.id
        existingPullRequest.title = pull_request.title
        existingPullRequest.number = pull_request.number
        existingPullRequest.state = PrState.valueOf(pull_request.state)
        existingPullRequest.url = pull_request.html_url
        existingPullRequest.merged = pull_request.merged
        existingPullRequest.githubUser = pull_request.user.toJsonB()
        existingPullRequest.assignees = pull_request.assignees.toJsonB()
        existingPullRequest.openedAt = pull_request.created_at
        existingPullRequest.repoName = repository.full_name
        existingPullRequest.repoUrl = repository.html_url
        existingPullRequest.store()
        return@transactionResult true
    }
    if (isExistingPR) return
    val associatedProfileId = context.fetchOne(
        PROFILE.where(PROFILE.GITHUB_UNIQUE_ID.eq(pull_request.user.id.toString()))
    )?.id
    val newPullRequest = context.newRecord(PULL_REQUEST)
    newPullRequest.githubPrId = pull_request.id
    newPullRequest.profileId = associatedProfileId
    newPullRequest.title = pull_request.title
    newPullRequest.number = pull_request.number
    newPullRequest.state = PrState.valueOf(pull_request.state)
    newPullRequest.url = pull_request.html_url
    newPullRequest.merged = pull_request.merged
    newPullRequest.githubUser = pull_request.user.toJsonB()
    newPullRequest.assignees = pull_request.assignees.toJsonB()
    newPullRequest.openedAt = pull_request.created_at
    newPullRequest.repoName = repository.full_name
    newPullRequest.repoUrl = repository.html_url
    newPullRequest.store()
}
