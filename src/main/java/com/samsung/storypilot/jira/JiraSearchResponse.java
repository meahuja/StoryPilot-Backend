package com.samsung.storypilot.jira;

import lombok.Data;
import java.util.List;

@Data
public class JiraSearchResponse {
    private List<JiraIssueWithStatus> issues;
}

@Data
class JiraIssueWithStatus {
    private String id;
    private String key;
    private JiraFields fields;
}

@Data
class JiraFields {
    private String summary;
    private JiraDescription description;
    private JiraStatus status;
}

@Data
class JiraStatus {
    private String name;
}
//
@Data
class JiraDescription {
    private String type;
    private int version;
    private List<Content> content;
}

@Data
class Content {
    private String type;
    private List<ContentText> content;
}

@Data
class ContentText {
    private String type;
    private String text;
}
