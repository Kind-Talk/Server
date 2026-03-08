package com.kindtalk.server.child.repository;

import com.kindtalk.server.child.domain.Child;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

  @EntityGraph(attributePaths = {"school"})
  List<Child> findAllByMemberId(Long id);

  Optional<Child> findByIdAndMemberId(Long childId, Long parentId);
}
