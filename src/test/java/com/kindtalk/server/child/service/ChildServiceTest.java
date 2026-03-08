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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ChildServiceTest {

  @Autowired
  ChildService childService;

  @Autowired
  ChildRepository childRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  SchoolRepository schoolRepository;

  private Member member;
  private School school;
  private MyUserDetails auth;

  @BeforeEach
  void setUp() {
    school = schoolRepository.save(new School("12345", "테스트 초등학교"));
    member = memberRepository.save(new Member("m1@email.com", "1234", "회원1", "별명1", null, Role.PARENT));
    auth = new MyUserDetails(member);
  }

  @AfterEach
  void delete() {
    childRepository.deleteAll();
    memberRepository.deleteAll();
    schoolRepository.deleteAll();
  }

  @Test
  void 자녀_등록_테스트() {
    // given
    ChildCreateRequest request = new ChildCreateRequest("자녀1", "12345");

    // when
    ChildResponse response = childService.create(auth, request);

    // then
    assertAll(
      () -> assertThat(response.name()).isEqualTo("자녀1"),
      () -> assertThat(response.schoolCode()).isEqualTo("12345"),
      () -> assertThat(response.schoolName()).isEqualTo("테스트 초등학교")
    );
  }

  @Test
  void 부모_권한이_아니면_자녀_등록_실패() {
    // given
    Member member1 = memberRepository.save(new Member("m2@email.com", "1234", "회원2", "별명2", school, Role.TEACHER));
    ChildCreateRequest request = new ChildCreateRequest("자녀1", "12345");
    MyUserDetails auth1 = new MyUserDetails(member1);

    // then
    BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
      childService.create(auth1, request);
    });
    assertThat(exception.getMessage()).isEqualTo("부모 권한을 가진 사용자만 자녀를 등록할 수 있습니다.");
  }

  @Test
  void 자녀_목록_조회_테스트() {
    // given
    School school1 = schoolRepository.save(new School("22222", "테스트2 초등학교"));

    Child child1 = childRepository.save(new Child("자녀3", member, school));
    Child child2 = childRepository.save(new Child("자녀4", member, school1)); // 다른 학교 등록

    // when
    List<ChildResponse> children = childService.findAll(auth);

    // then
    assertThat(children)
      .isNotEmpty()
      .hasSize(2)
      .extracting(
        ChildResponse::id,
        ChildResponse::parentId,
        ChildResponse::name,
        ChildResponse::schoolName)
      .containsExactlyInAnyOrder(
        tuple(child1.getId(), auth.getMemberId(), "자녀3", "테스트 초등학교"),
        tuple(child2.getId(), auth.getMemberId(), "자녀4", "테스트2 초등학교")
      );
  }

  @Test
  void 자녀_상세_조회_테스트() {
    // given
    Child child = childRepository.save(new Child("자녀5", member, school));

    // when
    ChildResponse response1 = childService.detail(child.getId(), auth);

    // then
    assertAll(
      () -> assertThat(response1.parentId()).isEqualTo(auth.getMemberId()),
      () -> assertThat(response1.name()).isEqualTo("자녀5"),
      () -> assertThat(response1.schoolCode()).isEqualTo("12345"),
      () -> assertThat(response1.schoolName()).isEqualTo("테스트 초등학교")
    );
  }

  @Test
  void 존재하지_않는_자녀_조회_시_실패() {
    // when & then
    DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
      childService.detail(100L, auth);
    });
    assertThat(exception.getMessage()).isEqualTo("해당 부모의 자녀가 존재하지 않습니다.");
  }

  @Test
  void 자녀_정보_수정_테스트() {
    // given
    schoolRepository.save(new School("33333", "테스트3 초등학교"));
    Child child = childRepository.save(new Child("자녀6", member, school));

    ChildUpdateRequest request = new ChildUpdateRequest("개명함", "33333");

    // when
    ChildResponse response1 = childService.update(child.getId(), auth, request);

    // then
    assertAll(
      () -> assertThat(response1.parentId()).isEqualTo(auth.getMemberId()),
      () -> assertThat(response1.name()).isEqualTo("개명함"),
      () -> assertThat(response1.schoolCode()).isEqualTo("33333"),
      () -> assertThat(response1.schoolName()).isEqualTo("테스트3 초등학교")
    );
  }

  @Test
  void 자녀_삭제_테스트() {
    // given
    Child child = childRepository.save(new Child("자녀6", member, school));

    // when
    childService.delete(child.getId(), auth);

    // then
    DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
      childService.detail(child.getId(), auth);
    });
    assertThat(exception.getMessage()).isEqualTo("해당 부모의 자녀가 존재하지 않습니다.");
  }
}
