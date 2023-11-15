package de.bearingpoint.app.capacityplanner.api;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.user.ApplicationUser;

import java.util.List;

public interface IssueManager {
    //This API deals with the query of issues with JQL

    // Search for issues assigned to current user
    public List<Issue> getIssuesInProject(ApplicationUser user) throws SearchException;

    // Search for issues in multiple projects, with empty assignee or reporter
    public List<Issue> getIssuesInProjectsWithEmptyUsers(ApplicationUser user) throws SearchException;

    // Search for issues with parsed Query
    public List<Issue> getIssuesInQuery(ApplicationUser user) throws SearchException;

    // Search for issues in multiple projects, with customer name admin,
    // assigned to admin (this function is for custom search)
    public List<Issue> getIssuesInProjectsForCustomer(ApplicationUser user) throws SearchException;

}
