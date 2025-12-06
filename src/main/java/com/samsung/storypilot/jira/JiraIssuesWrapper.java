package com.samsung.storypilot.jira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssuesWrapper {
    private List<JiraIssueDto> issues;
}
