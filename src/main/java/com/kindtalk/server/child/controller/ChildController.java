package com.kindtalk.server.child.controller;

import com.kindtalk.server.child.dto.ChildCreateRequest;
import com.kindtalk.server.child.dto.ChildResponse;
import com.kindtalk.server.child.dto.ChildUpdateRequest;
import com.kindtalk.server.child.service.ChildService;
import com.kindtalk.server.security.principal.MyUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/children")
public class ChildController {

  private final ChildService childService;

  @PostMapping
  public ResponseEntity<ChildResponse> create(
    @AuthenticationPrincipal MyUserDetails auth,
    @Valid @RequestBody ChildCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(childService.create(auth, request));
  }

  @GetMapping
  public ResponseEntity<List<ChildResponse>> findAll(
    @AuthenticationPrincipal MyUserDetails auth) {
    return ResponseEntity.ok(childService.findAll(auth));
  }

  @GetMapping("/{childId}")
  public ResponseEntity<ChildResponse> detail(
    @AuthenticationPrincipal MyUserDetails auth,
    @PathVariable Long childId) {
    return ResponseEntity.ok(childService.detail(childId, auth));
  }

  @PatchMapping("/{childId}")
  public ResponseEntity<ChildResponse> update(
    @AuthenticationPrincipal MyUserDetails auth,
    @PathVariable Long childId,
    @Valid @RequestBody ChildUpdateRequest request) {
    return ResponseEntity.ok(childService.update(childId, auth, request));
  }

  @DeleteMapping("/{childId}")
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal MyUserDetails auth,
    @PathVariable Long childId) {
    childService.delete(childId, auth);
    return ResponseEntity.noContent().build();
  }
}
