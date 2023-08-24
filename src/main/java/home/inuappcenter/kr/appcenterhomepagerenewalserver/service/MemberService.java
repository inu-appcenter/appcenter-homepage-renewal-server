package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.MemberRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.MemberResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.MemberRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupService groupService;

    @Transactional
    public MemberResponseDto getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
        return MemberResponseDto.builder()
                .member_id(member.getMember_id())
                .name(member.getName())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .blogLink(member.getBlogLink())
                .email(member.getEmail())
                .gitRepositoryLink(member.getGitRepositoryLink())
                .build();
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setMember(memberRequestDto);
        Member saved_member = memberRepository.save(member);
        return MemberResponseDto.builder()
                .member_id(saved_member.getMember_id())
                .name(saved_member.getName())
                .description(saved_member.getDescription())
                .profileImage(saved_member.getProfileImage())
                .blogLink(saved_member.getBlogLink())
                .email(saved_member.getEmail())
                .gitRepositoryLink(saved_member.getGitRepositoryLink())
                .build();
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto) {
        Member found_member = memberRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
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
                .build();
    }

    public List<MemberResponseDto> findAllMember() {
        List<Member> found_members = memberRepository.findAll();
        return found_members.stream()
                .map(data -> data.toMemberResponseDto(data))
                .collect(Collectors.toList());
    }

    public String deleteMember(Long id) {
        Member found_member = memberRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
        if(!groupService.findMember(found_member)) {
            memberRepository.deleteById(id);
            return "member id ["+ id + "] has been deleted.";
        } else {
            return "The member [" + id + "] is part of a Group. Please delete the Group first";
        }
    }
}
