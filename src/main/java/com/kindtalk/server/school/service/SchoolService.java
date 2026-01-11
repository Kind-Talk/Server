package com.kindtalk.server.school.service;

import com.kindtalk.server.school.adapter.SchoolApiAdapter;
import com.kindtalk.server.school.domain.School;
import com.kindtalk.server.school.dto.SchoolApiRequest;
import com.kindtalk.server.school.dto.SchoolResponse;
import com.kindtalk.server.school.dto.SchoolSearchRequest;
import com.kindtalk.server.school.dto.SchoolUpdateResponse;
import com.kindtalk.server.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

  private final SchoolApiAdapter schoolApiAdapter;
  private final SchoolRepository schoolRepository;

  public SchoolUpdateResponse update() {
    List<SchoolApiRequest> list = schoolApiAdapter.fetchSchools();
    int count = newSchool(list);
    return new SchoolUpdateResponse(list.size(), count, "학교 동기화 완료");
  }

  @Transactional
  public int newSchool(List<SchoolApiRequest> schoolList) {
    Set<String> codes = schoolRepository.findAll().stream()
      .map(School::getCode)
      .collect(Collectors.toSet());

    List<School> toSave = schoolList.stream()
      .filter(data -> !codes.contains(data.code()))
      .map(SchoolApiRequest::toEntity)
      .toList();

    schoolRepository.saveAll(toSave);
    return toSave.size();
  }

  public List<SchoolResponse> getSearch(SchoolSearchRequest request) {
    List<School> schoolList = schoolRepository.findByNameContaining(request.search());
    return schoolList.stream().map(SchoolResponse::of).toList();
  }
}
