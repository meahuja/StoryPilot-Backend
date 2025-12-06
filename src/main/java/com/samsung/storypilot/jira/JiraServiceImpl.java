package com.samsung.storypilot.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiraServiceImpl implements JiraService{

    private final RestTemplate restTemplate;

    @Value("${jira.baseUrl}")
    private String baseUrl;

    @Value("${jira.apiToken}")
    private String token;

    @Value("${jira.email}")
    private String email;

    public JiraServiceImpl(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    @Override
    public CreateJiraIssueDto createJiraIssue(JiraIssueDto jiraAddIssueDto) {

        String url = baseUrl + "/rest/api/3/issue";

        // Encode email:apiToken in Base64
        String auth = Base64.getEncoder().encodeToString((email + ":" + token).getBytes());

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        // HttpEntity
        HttpEntity<JiraIssueDto> entity = new HttpEntity<>(jiraAddIssueDto, headers);

        // Send POST request
        ResponseEntity<CreateJiraIssueDto> response = restTemplate.postForEntity(url, entity, CreateJiraIssueDto.class);
        updateIssueStatus(response.getBody().getKey(),"41");
        return response.getBody();
    }

    @Override
    public void updateIssueStatus(String issueKey, String transitionId) {
        String url = baseUrl + "/rest/api/3/issue/" + issueKey + "/transitions";

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(email, token);

        // Request body
        Map<String, Object> transition = new HashMap<>();
        Map<String, String> transitionObj = new HashMap<>();
        transitionObj.put("id", transitionId);
        transition.put("transition", transitionObj);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(transition, headers);

        // Send request
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println("Response: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
    }

    @Override
    public JiraSearchResponse getAllIssues(String statusName) {
        String jql = "project=" + "SCRUM" + " AND status=\"" + statusName + "\"";
        String url = baseUrl + "/rest/api/3/search?jql=" + jql;
        String auth = Base64.getEncoder().encodeToString((email + ":" + token).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<JiraSearchResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, JiraSearchResponse.class);

        return response.getBody();
    }
}
