package com.samsung.storypilot.jira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;
    @GetMapping("/test")
    public String Test(){
        return "Testing done";
    }

    @PostMapping("/issue")
    public CreateJiraIssueDto IssueId(@RequestBody JiraIssueDto jiraAddIssueDto)
    {
        return jiraService.createJiraIssue(jiraAddIssueDto);
    }

    @PostMapping("/updateStatus/{issueKey}")
    public String updateIssueStatus(
            @PathVariable String issueKey,@PathVariable String transitionId) {

        jiraService.updateIssueStatus(issueKey,transitionId);
        return "Status updated for issue: " + issueKey;
    }
    @GetMapping("/getAll/{statusName}")
    public JiraSearchResponse updateIssueStatus(
            @PathVariable String statusName) {

        JiraSearchResponse jiraSearchResponse = jiraService.getAllIssues(statusName);
        return jiraSearchResponse;
    }
}
