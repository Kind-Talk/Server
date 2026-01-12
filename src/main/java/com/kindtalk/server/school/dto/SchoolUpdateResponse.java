package com.kindtalk.server.school.dto;

public record SchoolUpdateResponse(
  int totalProcessed,
  int newSaved,
  String message) {
}
