package com.kindtalk.server.member.controller;


import com.kindtalk.server.config.security.MyUserDetails;
import com.kindtalk.server.member.dto.MemberJoinRequest;
import com.kindtalk.server.member.dto.MemberResponse;
import com.kindtalk.server.member.dto.MemberUpdateRequest;
import com.kindtalk.server.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<MemberResponse> memberJoin(
        @Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.memberJoin(memberJoinRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> memberDetail(
        @AuthenticationPrincipal MyUserDetails member) {
        return ResponseEntity.ok(memberService.memberDetail(member));
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberResponse> memberUpdate(
        @AuthenticationPrincipal MyUserDetails member,
        @Valid @RequestBody MemberUpdateRequest updateRequest) {
        return ResponseEntity.ok(memberService.memberUpdate(member, updateRequest));
    }
}
