/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db;


import com.wehuddle.db.tables.Answer;
import com.wehuddle.db.tables.FlywaySchemaHistory;
import com.wehuddle.db.tables.Issue;
import com.wehuddle.db.tables.IssueAssignment;
import com.wehuddle.db.tables.Profile;
import com.wehuddle.db.tables.PullRequest;
import com.wehuddle.db.tables.Session;
import com.wehuddle.db.tables.Task;
import com.wehuddle.db.tables.records.AnswerRecord;
import com.wehuddle.db.tables.records.FlywaySchemaHistoryRecord;
import com.wehuddle.db.tables.records.IssueAssignmentRecord;
import com.wehuddle.db.tables.records.IssueRecord;
import com.wehuddle.db.tables.records.ProfileRecord;
import com.wehuddle.db.tables.records.PullRequestRecord;
import com.wehuddle.db.tables.records.SessionRecord;
import com.wehuddle.db.tables.records.TaskRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AnswerRecord> ANSWER_PKEY = Internal.createUniqueKey(Answer.ANSWER, DSL.name("answer_pkey"), new TableField[] { Answer.ANSWER.ID }, true);
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<IssueRecord> ISSUE_GITHUB_ISSUE_ID_KEY = Internal.createUniqueKey(Issue.ISSUE, DSL.name("issue_github_issue_id_key"), new TableField[] { Issue.ISSUE.GITHUB_ISSUE_ID }, true);
    public static final UniqueKey<IssueRecord> ISSUE_PKEY = Internal.createUniqueKey(Issue.ISSUE, DSL.name("issue_pkey"), new TableField[] { Issue.ISSUE.ID }, true);
    public static final UniqueKey<IssueAssignmentRecord> ISSUE_ASSIGNMENT_ISSUE_ID_PROFILE_ID_KEY = Internal.createUniqueKey(IssueAssignment.ISSUE_ASSIGNMENT, DSL.name("issue_assignment_issue_id_profile_id_key"), new TableField[] { IssueAssignment.ISSUE_ASSIGNMENT.ISSUE_ID, IssueAssignment.ISSUE_ASSIGNMENT.PROFILE_ID }, true);
    public static final UniqueKey<IssueAssignmentRecord> ISSUE_ASSIGNMENT_PKEY = Internal.createUniqueKey(IssueAssignment.ISSUE_ASSIGNMENT, DSL.name("issue_assignment_pkey"), new TableField[] { IssueAssignment.ISSUE_ASSIGNMENT.ID }, true);
    public static final UniqueKey<ProfileRecord> PROFILE_PKEY = Internal.createUniqueKey(Profile.PROFILE, DSL.name("profile_pkey"), new TableField[] { Profile.PROFILE.ID }, true);
    public static final UniqueKey<PullRequestRecord> PULL_REQUEST_GITHUB_PR_ID_KEY = Internal.createUniqueKey(PullRequest.PULL_REQUEST, DSL.name("pull_request_github_pr_id_key"), new TableField[] { PullRequest.PULL_REQUEST.GITHUB_PR_ID }, true);
    public static final UniqueKey<PullRequestRecord> PULL_REQUEST_PKEY = Internal.createUniqueKey(PullRequest.PULL_REQUEST, DSL.name("pull_request_pkey"), new TableField[] { PullRequest.PULL_REQUEST.ID }, true);
    public static final UniqueKey<SessionRecord> SESSION_PKEY = Internal.createUniqueKey(Session.SESSION, DSL.name("session_pkey"), new TableField[] { Session.SESSION.ID }, true);
    public static final UniqueKey<TaskRecord> TASK_PKEY = Internal.createUniqueKey(Task.TASK, DSL.name("task_pkey"), new TableField[] { Task.TASK.ID }, true);
    public static final UniqueKey<TaskRecord> TASK_TITLE_KEY = Internal.createUniqueKey(Task.TASK, DSL.name("task_title_key"), new TableField[] { Task.TASK.TITLE }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<AnswerRecord, ProfileRecord> ANSWER__ANSWER_PROFILEID_FKEY = Internal.createForeignKey(Answer.ANSWER, DSL.name("answer_profileid_fkey"), new TableField[] { Answer.ANSWER.PROFILEID }, Keys.PROFILE_PKEY, new TableField[] { Profile.PROFILE.ID }, true);
    public static final ForeignKey<AnswerRecord, TaskRecord> ANSWER__ANSWER_TASKID_FKEY = Internal.createForeignKey(Answer.ANSWER, DSL.name("answer_taskid_fkey"), new TableField[] { Answer.ANSWER.TASKID }, Keys.TASK_PKEY, new TableField[] { Task.TASK.ID }, true);
    public static final ForeignKey<PullRequestRecord, ProfileRecord> PULL_REQUEST__PULL_REQUEST_PROFILE_ID_FKEY = Internal.createForeignKey(PullRequest.PULL_REQUEST, DSL.name("pull_request_profile_id_fkey"), new TableField[] { PullRequest.PULL_REQUEST.PROFILE_ID }, Keys.PROFILE_PKEY, new TableField[] { Profile.PROFILE.ID }, true);
    public static final ForeignKey<SessionRecord, ProfileRecord> SESSION__SESSION_PROFILE_ID_FKEY = Internal.createForeignKey(Session.SESSION, DSL.name("session_profile_id_fkey"), new TableField[] { Session.SESSION.PROFILE_ID }, Keys.PROFILE_PKEY, new TableField[] { Profile.PROFILE.ID }, true);
}
