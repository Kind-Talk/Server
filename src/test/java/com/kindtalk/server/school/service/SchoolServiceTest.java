package com.kindtalk.server.school.service;

import com.kindtalk.server.school.domain.School;
import com.kindtalk.server.school.dto.SchoolResponse;
import com.kindtalk.server.school.dto.SchoolSearchRequest;
import com.kindtalk.server.school.dto.SchoolUpdateResponse;
import com.kindtalk.server.school.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("h2")
public class SchoolServiceTest {

  @Autowired
  SchoolService schoolService;

  @Autowired
  SchoolRepository schoolRepository;

  @Test
  void 초등학교_동기화() {
    SchoolUpdateResponse response = schoolService.update();

    assertThat(response.message().equals("학교 동기화 완료"));
  }

  @Test
  void 초등학교_검색() {
    schoolRepository.save(new School("12345", "쿠키초등학교"));

    SchoolSearchRequest request = new SchoolSearchRequest("쿠키초");
    List<SchoolResponse> list = schoolService.getSearch(request);

    assertThat(list).isNotEmpty();
    assertThat(list)
      .extracting(SchoolResponse::name)
      .anyMatch(name -> name.contains("쿠키초등학교"));
  }
}
