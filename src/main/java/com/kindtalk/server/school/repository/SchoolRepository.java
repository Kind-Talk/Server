package com.kindtalk.server.school.repository;

import com.kindtalk.server.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, String> {

  List<School> findByNameContaining(String search);
}
