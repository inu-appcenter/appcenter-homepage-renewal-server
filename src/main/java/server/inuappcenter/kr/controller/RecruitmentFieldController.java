package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.RecruitmentFieldRequestDto;
import server.inuappcenter.kr.data.dto.response.RecruitmentFieldResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.RecruitmentFieldService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/recruitment-fields")
@RequiredArgsConstructor
@Tag(name = "[Recruitment Field] 모집 분야")
public class RecruitmentFieldController {

    private final RecruitmentFieldService recruitmentFieldService;

    @Operation(summary = "모집 분야 (1개) 조회", description = "조회할 모집 분야의 ID를 입력해주세요")
    @Parameter(name = "id", description = "모집 분야 ID", required = true)
    @GetMapping("/public/{id}")
    public ResponseEntity<RecruitmentFieldResponseDto> getField(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentFieldService.findField(id));
    }

    @Operation(summary = "모집 분야 (전체) 조회", description = "모든 모집 분야를 조회합니다")
    @GetMapping("/public/all")
    public ResponseEntity<List<RecruitmentFieldResponseDto>> getAllFields() {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentFieldService.findAllFields());
    }

    @Operation(summary = "모집 분야 저장", description = "새로운 모집 분야를 저장합니다")
    @PostMapping
    public ResponseEntity<CommonResponseDto> saveField(
            final @RequestBody @Valid RecruitmentFieldRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(recruitmentFieldService.saveField(requestDto));
    }

    @Operation(summary = "모집 분야 수정", description = "모집 분야 정보를 수정합니다")
    @Parameter(name = "id", description = "모집 분야 ID", required = true)
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponseDto> updateField(
            final @PathVariable("id") Long id,
            final @RequestBody @Valid RecruitmentFieldRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentFieldService.updateField(id, requestDto));
    }

    @Operation(summary = "모집 분야 삭제", description = "모집 분야를 삭제합니다")
    @Parameter(name = "id", description = "모집 분야 ID", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteField(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(recruitmentFieldService.deleteField(id));
    }
}
