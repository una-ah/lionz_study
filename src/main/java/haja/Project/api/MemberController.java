package haja.Project.api;

import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.Authority;
import haja.Project.domain.Member;
import haja.Project.domain.Part;
import haja.Project.service.MemberService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "멤버 수정")
    @PutMapping
    public MemberDto updateMember(@RequestBody @Valid MemberUpdateRequest request) {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        memberService.update(SecurityUtil.getCurrentMemberId(), request.phone_num, request.part, request.comment, request.major, request.student_id);
        return new MemberDto(member);
    }

    @Operation(summary = "로그인 중인 멤버 조회")
    @GetMapping
    public MemberDto MemberInfo() {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        return new MemberDto(member);
    }

    @Operation(summary = "id로 멤버 조회")
    @GetMapping("/{id}")
    public MemberDto findMemberInfoById(@PathVariable("id") Long id) {
        return new MemberDto(memberService.findById(id).get());
    }

    @Operation(summary = "전체 멤버 조회")
    @GetMapping("/all")
    public Result findAllMember() {
        List<Member> members = memberService.findAll();
        List<MemberDto> memberResult = members.stream()
                .map(member -> new MemberDto(member))
                .collect(Collectors.toList());
        return new Result(memberResult);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


    @Data
    static class MemberUpdateRequest {
        String phone_num;
        Part part;
        String comment;
        String major;
        String student_id;

    }
    @Data
    static class MemberDto {
        Long id;
        String email;
        Authority authority;
        String phone_num;
        Part part;
        String comment;
        String major;
        String student_id;

        public MemberDto(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.authority = member.getAuthority();
            this.phone_num = member.getPhone_num();
            this.part = member.getPart();
            this.comment = member.getComment();
            this.major = member.getMajor();
            this.student_id = member.getStudent_id();
        }
    }
}
