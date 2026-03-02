package com.kindtalk.server.member.service;

import com.kindtalk.server.exception.BusinessRuleException;
import com.kindtalk.server.exception.DataAlreadyExistsException;
import com.kindtalk.server.exception.DataNotFoundException;
import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.dto.MemberJoinRequest;
import com.kindtalk.server.member.dto.MemberResponse;
import com.kindtalk.server.member.dto.MemberUpdateRequest;
import com.kindtalk.server.member.dto.TeacherSchoolUpdateRequest;
import com.kindtalk.server.member.repository.MemberRepository;
import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.school.domain.School;
import com.kindtalk.server.school.repository.SchoolRepository;
import com.kindtalk.server.security.principal.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final SchoolRepository schoolRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public MemberResponse memberJoin(MemberJoinRequest join) {
    if (memberRepository.existsByEmail(join.email())) {
      throw new DataAlreadyExistsException("이미 존재하는 회원입니다.");
    }

    School school = null;
    if (join.role() == Role.TEACHER) {
      if (join.schoolCode() == null || join.schoolCode().isBlank())
        throw new BusinessRuleException("선생님 권한에서 학교는 필수입니다.");

      school = findSchool(join.schoolCode());
    }

    String password = passwordEncoder.encode(join.password());
    Member member = memberRepository.save(join.toEntity(password, school));
    return MemberResponse.of(member);
  }

  private School findSchool(String schoolCode) {
    return schoolRepository.findById(schoolCode)
      .orElseThrow(() -> new DataNotFoundException("해당 학교가 존재하지 않습니다."));
  }

  @Transactional(readOnly = true)
  public MemberResponse memberDetail(MyUserDetails auth) {
    Member member = findById(auth.getMember().getId());
    return MemberResponse.of(member);
  }

  @Transactional
  public MemberResponse memberUpdate(MyUserDetails auth, MemberUpdateRequest update) {
    Member member = findById(auth.getMember().getId());
    member.updateInfo(update.userName(), update.nickName());
    return MemberResponse.of(member);
  }

  private Member findById(Long id) {
    return memberRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("해당 회원이 존재하지 않습니다."));
  }

  @Transactional
  public MemberResponse updateSchool(MyUserDetails auth, TeacherSchoolUpdateRequest request) {
    Member member = findById(auth.getMemberId());

    if (member.getRole() != Role.TEACHER) throw new BusinessRuleException("선생님만 학교 정보를 수정할 수 있습니다.");

    School school = findSchool(request.schoolCode());
    member.updateSchool(school);
    return MemberResponse.of(member);
  }
}
