package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.RecruitmentField;
import server.inuappcenter.kr.data.domain.RecruitmentFieldMapping;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.domain.board.Recruitment;
import server.inuappcenter.kr.data.domain.board.RecruitmentStatus;
import server.inuappcenter.kr.data.dto.request.RecruitmentRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentFieldResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentListResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentResponseDto;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.repository.RecruitmentFieldMappingRepository;
import server.inuappcenter.kr.data.repository.RecruitmentFieldRepository;
import server.inuappcenter.kr.data.repository.RecruitmentRepository;
import server.inuappcenter.kr.data.repository.UserRepository;
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
    private final ImageRedisRepository imageRedisRepository;
    private final UserRepository userRepository;
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
                .status(recruitment.getStatus())
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
                .status(recruitment.getStatus())
                .dDay(recruitment.getDDay())
                .fieldNames(fieldNames)
                .build();
    }

    @Transactional
    public CommonResponseDto saveRecruitment(String uid, RecruitmentRequestDto requestDto) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new CustomNotFoundException("사용자를 찾을 수 없습니다."));

        Recruitment recruitment = new Recruitment(requestDto, user);
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);

        if (requestDto.getFieldIds() != null && !requestDto.getFieldIds().isEmpty()) {
            saveFieldMappings(savedRecruitment, requestDto.getFieldIds());
        }

        return new CommonResponseDto(savedRecruitment.getId() + " Recruitment has been successfully saved.");
    }

    @Transactional
    public CommonResponseDto updateRecruitment(String uid, Long recruitmentId, RecruitmentRequestDto requestDto) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        validateOwnership(uid, recruitment);
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
    public CommonResponseDto updateThumbnail(String uid, Long recruitmentId, MultipartFile thumbnail) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        validateOwnership(uid, recruitment);

        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new IllegalArgumentException("대표 이미지가 비어있을 수 없습니다.");
        }

        Long oldThumbnailId = recruitment.getThumbnail() != null ? recruitment.getThumbnail().getId() : null;

        recruitment.updateThumbnail(thumbnail);
        recruitmentRepository.save(recruitment);

        // 기존 이미지 Redis 캐시 삭제
        if (oldThumbnailId != null) {
            imageRedisRepository.deleteById(oldThumbnailId);
            log.info("리크루팅 썸네일 이미지 Redis 캐시 삭제: imageId={}", oldThumbnailId);
        }

        log.info("리크루팅 썸네일 수정 완료: recruitmentId={}, thumbnailId={}",
                recruitmentId,
                recruitment.getThumbnail() != null ? recruitment.getThumbnail().getId() : "없음");
        return new CommonResponseDto("thumbnail has been successfully modified.");
    }

    @Transactional
    public CommonResponseDto deleteRecruitment(String uid, Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        validateOwnership(uid, recruitment);

        recruitmentFieldMappingRepository.deleteAllByRecruitment(recruitment);
        recruitmentRepository.delete(recruitment);

        log.info("Recruitment 삭제 완료: id={}", recruitmentId);
        return new CommonResponseDto("id: " + recruitmentId + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto changeStatus(String uid, Long recruitmentId, RecruitmentStatus status) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 리크루팅 게시글이 없습니다."));

        validateOwnership(uid, recruitment);

        recruitment.updateManualStatus(status);
        recruitmentRepository.save(recruitment);

        log.info("Recruitment 상태 변경: id={}, manualStatus={}", recruitmentId, status);
        return new CommonResponseDto("id: " + recruitmentId + " 상태가 '" + status + "'(으)로 변경되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<RecruitmentListResponseDto> findMyRecruitments(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new CustomNotFoundException("사용자를 찾을 수 없습니다."));

        List<Recruitment> myRecruitments = recruitmentRepository.findAllByCreatedBy(user);
        List<RecruitmentListResponseDto> responseDtoList = new ArrayList<>();
        for (Recruitment recruitment : myRecruitments) {
            responseDtoList.add(buildListResponse(recruitment));
        }
        return responseDtoList;
    }

    private void validateOwnership(String uid, Recruitment recruitment) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new CustomNotFoundException("사용자를 찾을 수 없습니다."));
        // 관리자는 항상 허용
        if (user.isAdmin()) {
            return;
        }
        // 작성자 본인만 허용
        if (recruitment.getCreatedBy() == null || recruitment.getCreatedBy().getId() != user.getId()) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정/삭제할 수 있습니다.");
        }
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
