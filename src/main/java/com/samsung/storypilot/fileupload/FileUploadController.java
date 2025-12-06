package com.samsung.storypilot.fileupload;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsung.storypilot.ai.OpenAIService;
import com.samsung.storypilot.jira.JiraIssueDto;
import com.samsung.storypilot.jira.JiraIssuesWrapper;
import com.samsung.storypilot.jira.JiraServiceImpl;
import com.samsung.storypilot.util.WordTextExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin
public class FileUploadController {

    @Autowired
    WordTextExtractor wordTextExtractor;

    @Autowired
    OpenAIService openAIService;

    @Autowired
    JiraServiceImpl jiraServiceImpl;


    @PostMapping(
            value = "/upload-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file,
                                        @RequestParam("jsonPrompt") String jsonPrompt,
                                        @RequestParam(value = "uploadedBy", required = false) String uploadedBy) {
        try {
            if (file.isEmpty()) {
                //log.warn("File upload failed: empty file");
                return ResponseEntity.badRequest().body("File is empty");
            }

            // 1. Extract text
            String extractedText = wordTextExtractor.extractText(file.getInputStream());

            // 2. Call OpenAI service
            String jsonResponse = openAIService.generateJsonFromText(extractedText, jsonPrompt);

            if (jsonResponse == null || jsonResponse.isBlank()) {
                //log.error("OpenAI failed to generate JSON for file: {}", file.getOriginalFilename());
                return ResponseEntity.internalServerError().body("Failed to generate JSON from OpenAI");
            }

            //JIRA integration
            jsonResponse = jsonResponse.replace("```json","").replace("```","");
            System.out.print(jsonResponse);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            JiraIssuesWrapper wrapper = mapper.readValue(jsonResponse, JiraIssuesWrapper.class);
            List<JiraIssueDto> dtoList = wrapper.getIssues();
            for (JiraIssueDto issue : dtoList) {

                jiraServiceImpl.createJiraIssue(issue);
            }
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(jsonResponse);
//            JsonNode issuesArray = rootNode.get("issues");
////
////            // Check if issuesArray is present and is an array
//            if (issuesArray != null && issuesArray.isArray()) {
//                for (JsonNode issue : issuesArray) {
//                    System.out.println(issue);
//                    JiraIssueDto dto = mapper.treeToValue(issue, JiraIssueDto.class);
//                    jiraServiceImpl.createJiraIssue(dto);
////                    // Now 'issue' is a single JsonNode for each issue object
//                    System.out.println(issue);
////                    // You can also convert to a POJO if you have a class:
////                    // IssuePojo pojo = mapper.treeToValue(issue, IssuePojo.class);
//                }
//            }


            // 3. Prepare DTO for DB logging
//            FileUploadDTO fileUploadDTO = new FileUploadDTO();
//            fileUploadDTO.setId(System.currentTimeMillis()); // Or use @GeneratedValue in entity
//            fileUploadDTO.setFileName(file.getOriginalFilename());
//            fileUploadDTO.setFileSize(file.getSize());
//            fileUploadDTO.setStatus("Success");
//            fileUploadDTO.setUploadedBy(uploadedBy != null ? uploadedBy : "system");
//            fileUploadDTO.setUploadedDate(new Date());

            // 4. Save to DB
            //FileUploadDTO saved = fileUploadService.saveFile(fileUploadDTO);

            // 5. Log success
            //log.info("File [{}] uploaded successfully by [{}], size={} bytes", saved.getFileName(), saved.getUploadedBy(), saved.getFileSize());
            return ResponseEntity.ok(jsonResponse);

        } catch (Exception e) {
            // log.error("File upload failed: {}", file.getOriginalFilename(), e);

            // Save failed attempt as well
//            FileUploadDTO dto = new FileUploadDTO();
//            dto.setId(System.currentTimeMillis());
//            dto.setFileName(file.getOriginalFilename());
//            dto.setFileSize(file.getSize());
//            dto.setStatus("Failed");
//            dto.setUploadedBy(uploadedBy != null ? uploadedBy : "system");
//            dto.setUploadedDate(new Date());
//            fileUploadService.saveFile(dto);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


}
