package com.kindtalk.server.school.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.kindtalk.server.exception.BadGatewayException;
import com.kindtalk.server.school.dto.SchoolApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchoolApiAdapter {

  private static final String PARAM_TYPE = "Type";
  private static final String PARAM_INDEX = "pIndex";
  private static final String PARAM_SIZE = "pSize";
  private static final String PARAM_KEY = "KEY";
  private static final String PARAM_SCHUL_KND_SC_NM = "SCHUL_KND_SC_NM";

  private static final int VALUE_SIZE = 1000;
  private static final String VALUE_JSON = "json";
  private static final String TARGET_SCHOOL_TYPE = "초등학교";

  private static final String JSON_SCHOOL_INFO = "schoolInfo";
  private static final String JSON_ROW = "row";
  private static final String JSON_SD_SCHUL_CODE = "SD_SCHUL_CODE";
  private static final String JSON_SCHUL_NM = "SCHUL_NM";
  private static final String JSON_RESULT = "RESULT";
  private static final String JSON_CODE = "CODE";
  private static final String JSON_MESSAGE = "MESSAGE";

  private static final String SUCCESS_CODE = "INFO-200";

  private final SchoolApiProperties schoolApiProperties;
  private final RestClient restClient;

  public List<SchoolApiRequest> fetchSchools() {
    List<SchoolApiRequest> schoolList = new ArrayList<>();
    int pIndex = 1;
    boolean hasMoreData = true;

    while (hasMoreData) {
      URI uri = createUri(pIndex);
      JsonNode root = fetchApiResponse(uri);
      List<SchoolApiRequest> list = getSchoolData(root);

      if (!list.isEmpty()) {
        schoolList.addAll(list);
        pIndex++;

      } else {
        validateResponse(root);
        hasMoreData = false;
      }
    }
    return schoolList;
  }

  private URI createUri(int pIndex) {
    return UriComponentsBuilder.fromUriString(schoolApiProperties.url())
      .queryParam(PARAM_TYPE, VALUE_JSON)
      .queryParam(PARAM_INDEX, pIndex)
      .queryParam(PARAM_SIZE, VALUE_SIZE)
      .queryParam(PARAM_SCHUL_KND_SC_NM, TARGET_SCHOOL_TYPE)
      .queryParam(PARAM_KEY, schoolApiProperties.key())
      .build()
      .encode(StandardCharsets.UTF_8)
      .toUri();
  }

  private JsonNode fetchApiResponse(URI uri) {
    return restClient.get()
      .uri(uri)
      .retrieve()
      .onStatus(HttpStatusCode::isError, (request, response) -> {
        throw new BadGatewayException("Api Error: " + response.getStatusCode());
      })
      .body(JsonNode.class);
  }

  private List<SchoolApiRequest> getSchoolData(JsonNode root) {
    List<SchoolApiRequest> list = new ArrayList<>();
    JsonNode rowNode = root.path(JSON_SCHOOL_INFO).path(1).path(JSON_ROW);

    if (rowNode.isArray()) {
      for (JsonNode node : rowNode) {
        String code = node.path(JSON_SD_SCHUL_CODE).asText();
        String name = node.path(JSON_SCHUL_NM).asText();

        if (!code.isBlank()) {
          list.add(new SchoolApiRequest(code, name));
        }
      }
    }
    return list;
  }

  private void validateResponse(JsonNode root) {
    JsonNode result = root.path(JSON_RESULT);
    String code = result.path(JSON_CODE).asText();
    String message = result.path(JSON_MESSAGE).asText();

    if (!code.equals(SUCCESS_CODE)) {
      throw new BadGatewayException(message);
    }
  }
}
