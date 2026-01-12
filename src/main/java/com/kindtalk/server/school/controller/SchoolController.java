package com.kindtalk.server.school.controller;

import com.kindtalk.server.school.dto.SchoolResponse;
import com.kindtalk.server.school.dto.SchoolSearchRequest;
import com.kindtalk.server.school.dto.SchoolUpdateResponse;
import com.kindtalk.server.school.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/school")
public class SchoolController {

  private final SchoolService schoolService;

  @PostMapping("/sync")
  public ResponseEntity<SchoolUpdateResponse> sync() {
    return ResponseEntity.ok(schoolService.sync());
  }

  @GetMapping
  public ResponseEntity<List<SchoolResponse>> search(
    @Valid SchoolSearchRequest request) {
    return ResponseEntity.ok(schoolService.getSearch(request));
  }
}
