package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.StackRequestDto;
import server.inuappcenter.kr.data.dto.response.StackResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.StackService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/stacks")
@RequiredArgsConstructor
@Tag(name = "[Stack] 기술 스택")
public class StackController {

    private final StackService stackService;

    @Operation(summary = "스택 (1개) 조회", description = "조회할 스택의 ID를 입력해주세요")
    @Parameter(name = "id", description = "스택 ID", required = true)
    @GetMapping("/public/{id}")
    public ResponseEntity<StackResponseDto> getStack(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stackService.findStack(id));
    }

    @Operation(summary = "스택 (전체) 조회", description = "모든 스택을 조회합니다")
    @GetMapping("/public/all")
    public ResponseEntity<List<StackResponseDto>> getAllStacks() {
        return ResponseEntity.status(HttpStatus.OK).body(stackService.findAllStacks());
    }

    @Operation(summary = "스택 저장", description = "새로운 스택을 저장합니다")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> saveStack(
            final @ModelAttribute @Valid StackRequestDto stackRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stackService.saveStack(stackRequestDto));
    }

    @Operation(summary = "스택 수정", description = "스택 정보를 수정합니다")
    @Parameter(name = "id", description = "스택 ID", required = true)
    @PatchMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateStack(
            final @PathVariable("id") Long id,
            final @ModelAttribute @Valid StackRequestDto stackRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(stackService.updateStack(id, stackRequestDto));
    }

    @Operation(summary = "스택 삭제", description = "스택을 삭제합니다")
    @Parameter(name = "id", description = "스택 ID", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteStack(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(stackService.deleteStack(id));
    }
}