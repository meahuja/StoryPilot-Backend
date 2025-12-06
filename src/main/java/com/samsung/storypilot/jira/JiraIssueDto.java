package com.samsung.storypilot.jira;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueDto {

    private Fields fields;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fields {
        private Project project;
        private String summary;
        private Description description;
        private IssueType issuetype;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {
        private String key;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IssueType {
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {
        private String type;          // "doc"
        private int version;          // 1
        private List<Content> content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String type;          // "paragraph"
        private List<TextContent> content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextContent {
        private String type;          // "text"
        private String text;          // actual description text
    }
}
