package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.RecruitmentField;
import server.inuappcenter.kr.data.domain.RecruitmentFieldMapping;
import server.inuappcenter.kr.data.domain.board.Recruitment;
import server.inuappcenter.kr.data.dto.request.RecruitmentRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentFieldResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentListResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentResponseDto;
import server.inuappcenter.kr.data.repository.RecruitmentFieldMappingRepository;
import server.inuappcenter.kr.data.repository.RecruitmentFieldRepository;
import server.inuappcenter.kr.data.repository.RecruitmentRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("RecruitmentServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentServiceImpl implements AdditionalBoardService {
    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentFieldRepository recruitmentFieldRepository;
    private final RecruitmentFieldMappingRepository recruitmentFieldMappingRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList(String topic) {
        List<BoardResponseDto> responseDtoList = new ArrayList<>();
        for (Recruitment recruitment : recruitmentRepository.findAll()) {
            responseDtoList.add(buildResponse(recruitment));
        }
        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public List<RecruitmentListResponseDto> findAllRecruitmentList() {
        List<RecruitmentListResponseDto> responseDtoList = new ArrayList<>();
        for (Recruitment recruitment : recruitmentRepository.findAll()) {
            responseDtoList.add(buildListResponse(recruitment));
        }
        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));
        return buildResponse(recruitment);
    }

    private RecruitmentResponseDto buildResponse(Recruitment recruitment) {
        List<RecruitmentFieldMapping> mappings = recruitmentFieldMappingRepository.findAllByRecruitment(recruitment);
        List<RecruitmentFieldResponseDto> fieldDtos = mappings.stream()
                .map(mapping -> RecruitmentFieldResponseDto.entityToDto(mapping.getField()))
                .collect(Collectors.toList());

        String thumbnailUrl = null;
        if (recruitment.getThumbnail() != null) {
            thumbnailUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + recruitment.getThumbnail().getId();
        }

        return RecruitmentResponseDto.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .body(recruitment.getBody())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .capacity(recruitment.getCapacity())
                .targetAudience(recruitment.getTargetAudience())
                .applyLink(recruitment.getApplyLink())
                .thumbnail(thumbnailUrl)
                .isRecruiting(recruitment.isRecruiting())
                .forceClosed(recruitment.isForceClosed())
                .dDay(recruitment.getDDay())
                .fields(fieldDtos)
                .createdDate(recruitment.getCreatedDate())
                .lastModifiedDate(recruitment.getLastModifiedDate())
                .build();
    }

    private RecruitmentListResponseDto buildListResponse(Recruitment recruitment) {
        List<RecruitmentFieldMapping> mappings = recruitmentFieldMappingRepository.findAllByRecruitment(recruitment);
        List<String> fieldNames = mappings.stream()
                .map(mapping -> mapping.getField().getName())
                .collect(Collectors.toList());

        String thumbnailUrl = null;
        if (recruitment.getThumbnail() != null) {
            thumbnailUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + recruitment.getThumbnail().getId();
        }

        return RecruitmentListResponseDto.builder()
                .id(recruitment.getId())
                .title(recruitment.getTitle())
                .thumbnail(thumbnailUrl)
                .isRecruiting(recruitment.isRecruiting())
                .dDay(recruitment.getDDay())
                .fieldNames(fieldNames)
                .build();
    }

    @Transactional
    public CommonResponseDto saveRecruitment(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = new Recruitment(requestDto);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        if (requestDto.getFieldIds() != null && !requestDto.getFieldIds().isEmpty()) {
            saveFieldMappings(savedRecruitment, requestDto.getFieldIds());
        }

        return new CommonResponseDto(savedRecruitment.getId() + " Recruitment has been successfully saved.");
    }

    @Transactional
    public CommonResponseDto updateRecruitment(Long recruitmentId, RecruitmentRequestDto requestDto) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        recruitment.updateRecruitment(requestDto);

        if (requestDto.getFieldIds() != null) {
            recruitmentFieldMappingRepository.deleteAllByRecruitment(recruitment);
            if (!requestDto.getFieldIds().isEmpty()) {
                saveFieldMappings(recruitment, requestDto.getFieldIds());
            }
        }

        recruitmentRepository.save(recruitment);
        return new CommonResponseDto("id: " + recruitmentId + " has been successfully modified.");
    }

    @Transactional
    public CommonResponseDto updateThumbnail(Long recruitmentId, MultipartFile thumbnail) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new IllegalArgumentException("대표 이미지가 비어있을 수 없습니다.");
        }

        recruitment.updateThumbnail(thumbnail);
        recruitmentRepository.save(recruitment);

        log.info("리크루팅 썸네일 수정 완료: recruitmentId={}", recruitmentId);
        return new CommonResponseDto("thumbnail has been successfully modified.");
    }

    @Transactional
    public CommonResponseDto deleteRecruitment(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        recruitmentFieldMappingRepository.deleteAllByRecruitment(recruitment);
        recruitmentRepository.delete(recruitment);

        log.info("Recruitment 삭제 완료: id={}", recruitmentId);
        return new CommonResponseDto("id: " + recruitmentId + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto toggleForceClosed(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        recruitment.toggleForceClosed();
        recruitmentRepository.save(recruitment);

        String status = recruitment.isForceClosed() ? "강제 마감" : "마감 해제 (날짜 기준)";
        log.info("Recruitment 강제 마감 토글: id={}, status={}", recruitmentId, status);
        return new CommonResponseDto("id: " + recruitmentId + " 상태가 '" + status + "'로 변경되었습니다.");
    }

    private void saveFieldMappings(Recruitment recruitment, List<Long> fieldIds) {
        for (Long fieldId : fieldIds) {
            RecruitmentField field = recruitmentFieldRepository.findById(fieldId)
                    .orElseThrow(() -> new CustomNotFoundException("ID: " + fieldId + "에 해당되는 모집 분야가 없습니다."));
            RecruitmentFieldMapping mapping = new RecruitmentFieldMapping(recruitment, field);
            recruitmentFieldMappingRepository.save(mapping);
        }
    }
}
