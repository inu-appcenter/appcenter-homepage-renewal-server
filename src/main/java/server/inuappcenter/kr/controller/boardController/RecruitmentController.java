package server.inuappcenter.kr.controller.boardController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.RecruitmentJsonRequestDto;
import server.inuappcenter.kr.data.dto.request.RecruitmentRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentListResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.boardService.impl.RecruitmentServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/recruitment")
@RequiredArgsConstructor
@Tag(name = "[Recruitment] 리크루팅")
public class RecruitmentController {
    private final RecruitmentServiceImpl recruitmentService;

    @Operation(summary = "리크루팅 (1개) 조회", description = "조회할 리크루팅의 ID를 입력해주세요. 전체 정보를 반환합니다.")
    @Parameter(name = "id", description = "리크루팅 ID")
    @GetMapping("/public/{id}")
    public ResponseEntity<BoardResponseDto> getRecruitment(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.findBoard(id));
    }

    @Operation(summary = "리크루팅 (전체) 목록 조회", description = "모든 리크루팅을 간략 정보로 조회합니다. (썸네일, 제목, D-day, 상태, 분야)")
    @GetMapping("/public/all")
    public ResponseEntity<List<RecruitmentListResponseDto>> getAllRecruitments() {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.findAllRecruitmentList());
    }

    @Operation(
            summary = "리크루팅 저장",
            description = "리크루팅을 저장합니다.\n"
                    + "- 요청은 multipart/form-data 로 전송합니다.\n"
                    + "- JSON 파트 이름은 request 이고 Content-Type은 application/json 으로 설정합니다.\n"
                    + "- fieldIds 에서는 모집분야 [Recruitment Field] 를 통한 Id 값을 리스트로 입력합니다."
                    + "- FILE 파트는 thumbnail(대표 이미지, 선택)입니다."
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> saveRecruitment(
            final @RequestPart("request") @Valid RecruitmentJsonRequestDto requestDto,
            final @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        RecruitmentRequestDto mappedRequest = mapToMultipartRequest(requestDto, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(recruitmentService.saveRecruitment(mappedRequest));
    }

    @Operation(
            summary = "리크루팅 메타데이터 수정",
            description = "리크루팅의 텍스트/메타데이터를 수정합니다.\n"
                    + "- 요청은 application/json 으로 전송합니다.\n"
                    + "- 이미지 변경은 별도 API를 사용합니다."
    )
    @PatchMapping(value = "/meta", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponseDto> updateRecruitmentMeta(
            final @RequestParam("recruitment_id") Long recruitmentId,
            final @RequestBody @Valid RecruitmentJsonRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        RecruitmentRequestDto mappedRequest = mapToMultipartRequest(requestDto, null);
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.updateRecruitment(recruitmentId, mappedRequest));
    }

    @Operation(
            summary = "대표 이미지 수정",
            description = "리크루팅의 대표 이미지를 수정합니다.\n"
                    + "- 요청은 multipart/form-data 로 전송합니다.\n"
                    + "- 파일 파트 이름은 thumbnail 입니다."
    )
    @PatchMapping(value = "/thumbnail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateThumbnail(
            final @RequestParam("recruitment_id") Long recruitmentId,
            final @RequestPart("thumbnail") MultipartFile thumbnail
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.updateThumbnail(recruitmentId, thumbnail));
    }

    @Operation(summary = "리크루팅 삭제", description = "삭제할 리크루팅의 ID를 입력해주세요")
    @Parameter(name = "id", description = "리크루팅 ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteRecruitment(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.deleteRecruitment(id));
    }

    @PreAuthorize("hasRole('MEMBER')")
    @Operation(summary = "내가 작성한 리크루팅 목록 조회", description = "로그인한 사용자가 작성한 리크루팅 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<RecruitmentListResponseDto>> getMyRecruitments(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.findMyRecruitments(userDetails.getUsername()));
    }

    @PreAuthorize("hasRole('MEMBER')")
    @Operation(
            summary = "강제 마감/마감 해제 토글",
            description = "리크루팅의 모집 상태를 강제로 토글합니다.\n"
                    + "- 강제 마감 상태면 마감일과 관계없이 '마감' 상태로 표시됩니다.\n"
                    + "- 다시 호출하면 마감 해제되어 날짜 기준으로 상태가 계산됩니다."
    )
    @Parameter(name = "id", description = "리크루팅 ID")
    @PatchMapping("/{id}/toggle-close")
    public ResponseEntity<CommonResponseDto> toggleForceClosed(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentService.toggleForceClosed(id));
    @Operation(summary = "이메일 등록", description = "리크루팅 관련 이메일 주소를 등록합니다. 중복 등록은 불가합니다.")
    @PostMapping("/public/email")
    public ResponseEntity<CommonResponseDto> subscribeEmail(
            @RequestBody @Valid EmailSubscriptionRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emailSubscriptionService.subscribe(requestDto.getEmail()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "수집된 이메일 목록 조회(관리자 전용)", description = "등록된 이메일 목록을 조회합니다. (관리자 전용)")
    @GetMapping("/email")
    public ResponseEntity<List<EmailSubscriptionResponseDto>> getAllEmails(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(emailSubscriptionService.findAll());
    }

    private RecruitmentRequestDto mapToMultipartRequest(RecruitmentJsonRequestDto requestDto, MultipartFile thumbnail) {
        return new RecruitmentRequestDto(
                requestDto.getTitle(),
                requestDto.getBody(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.getCapacity(),
                requestDto.getTargetAudience(),
                requestDto.getApplyLink(),
                thumbnail,
                requestDto.getFieldIds()
        );
    }
}
