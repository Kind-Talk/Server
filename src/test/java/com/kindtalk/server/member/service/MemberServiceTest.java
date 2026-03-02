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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  SchoolRepository schoolRepository;

  private Member member;
  private School school;

  @BeforeEach
  void setUp() {
    school = schoolRepository.save(new School("12345", "테스트 초등학교"));
    member = memberRepository.save(new Member("m1@email.com", "1234", "회원1", "별명1", school, Role.TEACHER)); // default teacher
  }

  @AfterEach
  void delete() {
    memberRepository.deleteAll();
  }

  @Test
  void 회원가입_테스트() {
    // given
    MemberJoinRequest request = new MemberJoinRequest("m2@email.com", "1234", "회원2", "별명2", "12345", Role.TEACHER);

    // when
    MemberResponse memberResponse = memberService.memberJoin(request);

    // then
    assertAll(
      () -> assertThat(memberResponse.email()).isEqualTo("m2@email.com"),
      () -> assertThat(memberResponse.userName()).isEqualTo("회원2"),
      () -> assertThat(memberResponse.nickName()).isEqualTo("별명2"),
      () -> assertThat(memberResponse.schoolCode()).isEqualTo("12345"),
      () -> assertThat(memberResponse.schoolName()).isEqualTo("테스트 초등학교"),
      () -> assertThat(memberResponse.role()).isEqualTo(Role.TEACHER)
    );
  }

  @Test
  void 선생님_학교_누락_시_회원가입_실패() {
    // given
    MemberJoinRequest request = new MemberJoinRequest("m2@email.com", "1234", "회원2", "별명2", null, Role.TEACHER);

    // when & then
    BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
      memberService.memberJoin(request);
    });
    assertThat(exception.getMessage()).isEqualTo("선생님 권한에서 학교는 필수입니다.");
  }

  @Test
  void 중복회원_테스트() {
    // given
    MemberJoinRequest request = new MemberJoinRequest("m1@email.com", "1234", "회원1", "별명1", "12345", Role.TEACHER);

    // when & then
    DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> {
      memberService.memberJoin(request);
    });
    assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
  }

  @Test
  void 회원조회_테스트() {
    // given
    MyUserDetails auth = new MyUserDetails(member);

    // when
    MemberResponse response = memberService.memberDetail(auth);

    // then
    assertAll(
      () -> assertThat(response.email()).isEqualTo("m1@email.com"),
      () -> assertThat(response.userName()).isEqualTo("회원1"),
      () -> assertThat(response.nickName()).isEqualTo("별명1"),
      () -> assertThat(response.schoolCode()).isEqualTo("12345"),
      () -> assertThat(response.schoolName()).isEqualTo("테스트 초등학교"),
      () -> assertThat(response.role()).isEqualTo(Role.TEACHER)
    );
  }

  @Test
  void 회원수정_테스트() {
    // given
    MyUserDetails auth = new MyUserDetails(member);
    MemberUpdateRequest request = new MemberUpdateRequest("개명함", "별명변경");

    // when
    MemberResponse response = memberService.memberUpdate(auth, request);

    // then
    assertAll(
      () -> assertThat(response.email()).isEqualTo("m1@email.com"),
      () -> assertThat(response.userName()).isEqualTo("개명함"),
      () -> assertThat(response.nickName()).isEqualTo("별명변경"),
      () -> assertThat(response.schoolCode()).isEqualTo("12345"),
      () -> assertThat(response.schoolName()).isEqualTo("테스트 초등학교"),
      () -> assertThat(response.role()).isEqualTo(Role.TEACHER)
    );
  }

  @Test
  void 없는_회원_조회하기_테스트() {
    // given
    Member mock = mock(Member.class);
    when(mock.getId()).thenReturn(100L);
    MyUserDetails auth = new MyUserDetails(mock);

    // when & then
    DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
      memberService.memberDetail(auth);
    });
    assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
  }

  @Test
  void 선생님_학교_수정_테스트() {
    // given
    schoolRepository.save(new School("22222", "테스트2 초등학교"));

    TeacherSchoolUpdateRequest request = new TeacherSchoolUpdateRequest("22222");
    MyUserDetails auth = new MyUserDetails(member);

    // when
    MemberResponse response = memberService.updateSchool(auth, request);

    // then
    assertAll(
      () -> assertThat(response.schoolCode()).isEqualTo("22222"),
      () -> assertThat(response.schoolName()).isEqualTo("테스트2 초등학교")
    );
  }
}
