package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.RecruitmentField;
import server.inuappcenter.kr.data.dto.request.RecruitmentFieldRequestDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentFieldResponseDto;
import server.inuappcenter.kr.data.repository.RecruitmentFieldRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentFieldService {
    private final RecruitmentFieldRepository recruitmentFieldRepository;

    @Transactional(readOnly = true)
    public RecruitmentFieldResponseDto findField(Long id) {
        RecruitmentField field = recruitmentFieldRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 모집 분야를 찾을 수 없습니다."));
        return RecruitmentFieldResponseDto.entityToDto(field);
    }

    @Transactional(readOnly = true)
    public List<RecruitmentFieldResponseDto> findAllFields() {
        return recruitmentFieldRepository.findAll().stream()
                .map(RecruitmentFieldResponseDto::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommonResponseDto saveField(RecruitmentFieldRequestDto requestDto) {
        if (recruitmentFieldRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 모집 분야 이름입니다.");
        }
        RecruitmentField field = new RecruitmentField(requestDto.getName());
        RecruitmentField savedField = recruitmentFieldRepository.save(field);
        return new CommonResponseDto("모집 분야 ID : " + savedField.getId() + "이 저장되었습니다.");
    }

    @Transactional
    public CommonResponseDto updateField(Long id, RecruitmentFieldRequestDto requestDto) {
        RecruitmentField field = recruitmentFieldRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 모집 분야를 찾을 수 없습니다."));

        if (!field.getName().equals(requestDto.getName()) && recruitmentFieldRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 모집 분야 이름입니다.");
        }

        field.updateName(requestDto.getName());
        recruitmentFieldRepository.save(field);
        return new CommonResponseDto("모집 분야 ID : " + id + "이 수정되었습니다.");
    }

    @Transactional
    public CommonResponseDto deleteField(Long id) {
        recruitmentFieldRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("해당 ID의 모집 분야를 찾을 수 없습니다."));
        recruitmentFieldRepository.deleteById(id);
        return new CommonResponseDto("모집 분야 ID : " + id + "이 삭제되었습니다.");
    }
}
