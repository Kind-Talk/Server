package com.kindtalk.server.child.service;

import com.kindtalk.server.child.domain.Child;
import com.kindtalk.server.child.dto.ChildCreateRequest;
import com.kindtalk.server.child.dto.ChildResponse;
import com.kindtalk.server.child.dto.ChildUpdateRequest;
import com.kindtalk.server.child.repository.ChildRepository;
import com.kindtalk.server.exception.BusinessRuleException;
import com.kindtalk.server.exception.DataNotFoundException;
import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.repository.MemberRepository;
import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.school.domain.School;
import com.kindtalk.server.school.repository.SchoolRepository;
import com.kindtalk.server.security.principal.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService {

  private final ChildRepository childRepository;
  private final MemberRepository memberRepository;
  private final SchoolRepository schoolRepository;

  @Transactional
  public ChildResponse create(MyUserDetails auth, ChildCreateRequest request) {
    Member member = findMember(auth.getMemberId());

    if (member.getRole() != Role.PARENT) {
      throw new BusinessRuleException("부모 권한을 가진 사용자만 자녀를 등록할 수 있습니다.");
    }

    School school = findSchool(request.schoolCode());
    Child child = childRepository.save(new Child(request.name(), member, school));
    return ChildResponse.of(child);
  }

  private Member findMember(Long memberId) {
    return memberRepository.findById(memberId)
      .orElseThrow(() -> new DataNotFoundException("해당 회원이 존재하지 않습니다."));
  }

  private School findSchool(String schoolCode) {
    return schoolRepository.findById(schoolCode)
      .orElseThrow(() -> new DataNotFoundException("해당 학교가 존재하지 않습니다."));
  }

  @Transactional(readOnly = true)
  public List<ChildResponse> findAll(MyUserDetails auth) {
    List<Child> children = childRepository.findAllByMemberId(auth.getMemberId());
    return children.stream().map(ChildResponse::of).toList();
  }

  @Transactional(readOnly = true)
  public ChildResponse detail(Long childId, MyUserDetails auth) {
    Child child = findChild(childId, auth.getMemberId());
    return ChildResponse.of(child);
  }

  private Child findChild(Long childId, Long parentId) {
    return childRepository.findByIdAndMemberId(childId, parentId)
      .orElseThrow(() -> new DataNotFoundException("해당 부모의 자녀가 존재하지 않습니다."));
  }

  @Transactional
  public ChildResponse update(Long childId, MyUserDetails auth, ChildUpdateRequest request) {
    Child child = findChild(childId, auth.getMemberId());
    School school = findSchool(request.schoolCode());
    child.updateInfo(request.name(), school);
    return ChildResponse.of(child);
  }

  @Transactional
  public void delete(Long childId, MyUserDetails auth) {
    Child child = findChild(childId, auth.getMemberId());
    childRepository.delete(child);
  }
}
