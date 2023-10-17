package server.inuappcenter.kr.service;

import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return MemberResponseDto.builder()
                .member_id(member.getMember_id())
                .name(member.getName())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .blogLink(member.getBlogLink())
                .email(member.getEmail())
                .gitRepositoryLink(member.getGitRepositoryLink())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .build();
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        Member saved_member = memberRepository.save(member);
        return MemberResponseDto.builder()
                .member_id(saved_member.getMember_id())
                .name(saved_member.getName())
                .description(saved_member.getDescription())
                .profileImage(saved_member.getProfileImage())
                .blogLink(saved_member.getBlogLink())
                .email(saved_member.getEmail())
                .gitRepositoryLink(saved_member.getGitRepositoryLink())
                .createdDate(saved_member.getCreatedDate())
                .lastModifiedDate(saved_member.getLastModifiedDate())
                .build();
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto) {
        Member found_member = memberRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        found_member.setMember(id, memberRequestDto);
        Member saved_member = memberRepository.save(found_member);
        return MemberResponseDto.builder()
                .member_id(saved_member.getMember_id())
                .name(saved_member.getName())
                .description(saved_member.getDescription())
                .profileImage(saved_member.getProfileImage())
                .blogLink(saved_member.getBlogLink())
                .email(saved_member.getEmail())
                .gitRepositoryLink(saved_member.getGitRepositoryLink())
                .createdDate(saved_member.getCreatedDate())
                .lastModifiedDate(saved_member.getLastModifiedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMember() {
        List<Member> found_members = memberRepository.findAll();
        return found_members.stream()
                .map(data -> data.toMemberResponseDto(data))
                .collect(Collectors.toList());
    }

    public CommonResponseDto deleteMember(Long id) {
        Member found_member = memberRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        ArrayList<Group> found_group = groupRepository.findAllByMember(found_member);
        if(found_group.isEmpty()) {
            memberRepository.deleteById(id);
            return new CommonResponseDto("member id ["+ id + "] has been deleted.");
        } else {
            return new CommonResponseDto("The member [" + id + "] is part of a Group. Please delete the Group first");
        }
    }
}
