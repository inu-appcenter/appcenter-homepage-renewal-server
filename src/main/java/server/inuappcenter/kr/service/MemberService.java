package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

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
        return MemberResponseDto.entityToDto(member);
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto);
        Member savedMember = memberRepository.save(member);
        return MemberResponseDto.entityToDto(savedMember);
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto) {
        Member found_member = memberRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        found_member.updateMember(id, memberRequestDto);
        Member saved_member = memberRepository.save(found_member);
        return MemberResponseDto.entityToDto(saved_member);
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

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findIdByName(String name) {
        List<Member> foundMembers = memberRepository.findAllByName(name);
        return foundMembers.stream()
                .map(data -> data.toMemberResponseDto(data))
                .collect(Collectors.toList());
    }
}
