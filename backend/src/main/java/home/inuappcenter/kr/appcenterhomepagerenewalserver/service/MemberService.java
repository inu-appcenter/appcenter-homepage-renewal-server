package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.MemberRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.MemberResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupService groupService;

    public MemberResponseDto getMember(Long id) throws Exception {
        Member member = memberRepository.findById(id)
                .orElseThrow(Exception::new);
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(member);
        return memberResponseDto;
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member();
        member.setMember(memberRequestDto);
        Member saved_member = memberRepository.save(member);
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(saved_member);
        return memberResponseDto;
    }

    public MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto) throws Exception {
        Member found_member = memberRepository.findById(id).orElseThrow(Exception::new);
        found_member.setMember(id, memberRequestDto);
        Member saved_member = memberRepository.save(found_member);
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(saved_member);
        return memberResponseDto;
    }

    public List<MemberResponseDto> findAllMember() {
        List<Member> found_members = memberRepository.findAll();
        return found_members.stream()
                .map(data -> data.toMemberResponseDto(data))
                .collect(Collectors.toList());
    }

    public String deleteMember(Long id) throws Exception {
        Member found_member = memberRepository.findById(id).orElseThrow(Exception::new);
        if(!groupService.findMember(found_member)) {
            memberRepository.deleteById(id);
            return "member id ["+ id + "] has been deleted.";
        } else {
            return "The member [" + id + "] is part of a Group. Please delete the Group first";
        }
    }
}
