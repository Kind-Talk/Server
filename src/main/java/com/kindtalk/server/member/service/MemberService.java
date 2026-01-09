package com.kindtalk.server.member.service;

import com.kindtalk.server.exception.DataAlreadyExistsException;
import com.kindtalk.server.exception.DataNotFoundException;
import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.dto.MemberJoinRequest;
import com.kindtalk.server.member.dto.MemberResponse;
import com.kindtalk.server.member.dto.MemberUpdateRequest;
import com.kindtalk.server.member.repository.MemberRepository;
import com.kindtalk.server.security.principal.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public MemberResponse memberJoin(MemberJoinRequest join) {
    if (memberRepository.existsByEmail(join.email())) {
      throw new DataAlreadyExistsException("이미 존재하는 회원입니다.");
    }
    String password = passwordEncoder.encode(join.password());
    Member member = memberRepository.save(join.toEntity(password));
    return MemberResponse.of(member);
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
}
