package com.kindtalk.server.member.service;

import com.kindtalk.server.exception.DataAlreadyExistsException;
import com.kindtalk.server.exception.DataNotFoundException;
import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.dto.MemberJoinRequest;
import com.kindtalk.server.member.dto.MemberResponse;
import com.kindtalk.server.member.dto.MemberUpdateRequest;
import com.kindtalk.server.member.repository.MemberRepository;
import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.security.principal.MyUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  MemberJoinRequest member;

  @BeforeEach
  void setUp() {
    member = new MemberJoinRequest(
      "m1@email.com",
      "11",
      "박땡땡",
      "박맘",
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
    String password = passwordEncoder.encode(member.password());
    memberRepository.save(member.toEntity(password));

    DataAlreadyExistsException exception = assertThrows(DataAlreadyExistsException.class, () -> {
      memberService.memberJoin(member);
    });
    assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
  }

  @Test
  void 회원조회_테스트() {
    String password = passwordEncoder.encode(member.password());
    Member memberData = memberRepository.save(member.toEntity(password));

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
    Member memberData = memberRepository.save(member.toEntity(password));

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
    Member memberEntity = member.toEntity(password);

    ReflectionTestUtils.setField(memberEntity, "id", 100L);
    MyUserDetails userDetails = new MyUserDetails(memberEntity);

    DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
      memberService.memberDetail(userDetails);
    });
    assertThat(exception.getMessage()).isEqualTo("해당 회원이 존재하지 않습니다.");
  }
}
