package com.samsung.storypilot.jira;

public interface JiraService {
    public CreateJiraIssueDto createJiraIssue(JiraIssueDto jiraAddIssueDto);
    public void updateIssueStatus(String issueKey, String transitionId);
    public JiraSearchResponse getAllIssues(String statusName);
}
