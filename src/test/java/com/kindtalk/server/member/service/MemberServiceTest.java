package com.kindtalk.server.member.service;

import com.kindtalk.server.exception.BadRequestException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  SchoolRepository schoolRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  private MemberJoinRequest member;
  private School school;

  @BeforeEach
  void setUp() {
    school = schoolRepository.save(new School("12345", "테스트 초등학교"));

    member = new MemberJoinRequest(
      "m1@email.com",
      "11",
      "박땡땡",
      "박맘",
      school.getCode(),
      Role.TEACHER
    );
  }

  @AfterEach
  void delete() {
    memberRepository.deleteAll();
  }

  @Test
  void 회원가입_테스트() {
    MemberResponse memberResponse = memberService.memberJoin(member);

    assertThat(memberResponse.email()).isEqualTo("m1@email.com");
    assertThat(memberResponse.userName()).isEqualTo("박땡땡");
    assertThat(memberResponse.nickName()).isEqualTo("박맘");
    assertThat(memberResponse.role()).isEqualTo(Role.TEACHER);
  }

  @Test
  void 중복회원_테스트() {
    memberRepository.save(new Member("m1@email.com", "11", "박땡땡", "박맘", school, Role.TEACHER));

    DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> {
      memberService.memberJoin(member);
    });
    assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
  }

  @Test
  void 선생님_학교_누락_시_회원가입_실패() {
    // given
    MemberJoinRequest request = new MemberJoinRequest("m1@email.com", "11", "회원1", "별명1", null, Role.TEACHER);

    // when & then
    BadRequestException exception = assertThrows(BadRequestException.class, () -> {
      memberService.memberJoin(request);
    });
    assertThat(exception.getMessage()).isEqualTo("선생님 권한에서 학교는 필수입니다.");
  }

  @Test
  void 회원조회_테스트() {
    String password = passwordEncoder.encode(member.password());
    Member memberData = memberRepository.save(member.toEntity(password, school));

    MyUserDetails userDetails = new MyUserDetails(memberData);

    MemberResponse memberResponse = memberService.memberDetail(userDetails);

    assertThat(memberResponse.email()).isEqualTo("m1@email.com");
    assertThat(memberResponse.userName()).isEqualTo("박땡땡");
    assertThat(memberResponse.nickName()).isEqualTo("박맘");
    assertThat(memberResponse.role()).isEqualTo(Role.TEACHER);
  }

  @Test
  void 회원수정_테스트() {
    String password = passwordEncoder.encode(member.password());
    Member memberData = memberRepository.save(member.toEntity(password, school));

    MyUserDetails userDetails = new MyUserDetails(memberData);

    MemberUpdateRequest updateRequest = new MemberUpdateRequest("박둘", "박파더");
    MemberResponse memberResponse = memberService.memberUpdate(userDetails, updateRequest);

    assertThat(memberResponse.email()).isEqualTo("m1@email.com");
    assertThat(memberResponse.userName()).isEqualTo("박둘");
    assertThat(memberResponse.nickName()).isEqualTo("박파더");
    assertThat(memberResponse.role()).isEqualTo(Role.TEACHER);
  }

  @Test
  void 없는_회원_조회하기_테스트() {
    String password = passwordEncoder.encode(member.password());
    Member memberEntity = member.toEntity(password, school); // ???

    ReflectionTestUtils.setField(memberEntity, "id", 100L);
    MyUserDetails userDetails = new MyUserDetails(memberEntity);

    DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
      memberService.memberDetail(userDetails);
    });
    assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
  }

  @Test
  void 선생님_학교_수정_테스트() {
    // given
    schoolRepository.save(new School("22222", "테스트2 초등학교"));
    Member member = memberRepository.save(new Member("m1@email.com", "1234", "회원1", "별명1", school, Role.TEACHER));

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
