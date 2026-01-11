package com.kindtalk.server.school.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindtalk.server.exception.BadGatewayException;
import com.kindtalk.server.school.dto.SchoolApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolApiAdapter {

  @Value("${school.api.url}")
  private String baseUrl;

  @Value("${school.api.key}")
  private String apiKey;

  private final ObjectMapper objectMapper;

  public List<SchoolApiRequest> fetchSchools() {
    int pIndex = 1;
    int pSize = 1000;
    boolean hasMoreData = true;
    List<SchoolApiRequest> schoolList = new ArrayList<>();

    RestTemplate restTemplate = new RestTemplate();

    while (hasMoreData) {
      URI uri = UriComponentsBuilder.fromUriString(baseUrl)
        .queryParam("Type", "json")
        .queryParam("pIndex", pIndex)
        .queryParam("pSize", pSize)
        .queryParam("SCHUL_KND_SC_NM", "초등학교")
        .queryParam("KEY", apiKey)
        .build()
        .encode(StandardCharsets.UTF_8)
        .toUri();

      try {
        String response = restTemplate.getForObject(uri, String.class);
        JsonNode root = objectMapper.readTree(response);
        JsonNode rowNode = root.path("schoolInfo").path(1).path("row");

        if (rowNode.isArray() && !rowNode.isEmpty()) {
          for (JsonNode node : rowNode) {
            String code = node.path("SD_SCHUL_CODE").asText();
            String name = node.path("SCHUL_NM").asText();

            if (!code.isBlank()) {
              schoolList.add(new SchoolApiRequest(code, name));
            }
          }
          pIndex++;
        } else {
          JsonNode result = root.path("RESULT");
          String code = result.path("CODE").asText();
          String message = result.path("MESSAGE").asText();

          if (!code.equals("INFO-200")) {
            throw new BadGatewayException(message);
          }
          hasMoreData = false;
        }
      } catch (RestClientException | IOException e) {
        throw new BadGatewayException("API Error: " + e.getMessage());
      }
    }
    return schoolList;
  }
}
