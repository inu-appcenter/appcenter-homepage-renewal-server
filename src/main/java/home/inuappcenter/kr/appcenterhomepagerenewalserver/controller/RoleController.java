package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.RoleRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.RoleResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "역할 (1개) 가져오기", description = "역할에게 부여된 id를 입력해주세요 / 역할 1개를 반환합니다.")
    @Parameter(name = "id", description = "역할 id", required = true)
    @GetMapping
    public ResponseEntity<RoleResponseDto> getRole(final Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Role을 요청했습니다.");
        RoleResponseDto roleResponseDto = roleService.getRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(roleResponseDto);
    }

    @Operation(summary = "역할 (전체) 가져오기", description = "전체 역할을 반환합니다.")
    @GetMapping("/all-roles")
    public ResponseEntity<List<RoleResponseDto>> getAllRole() {
        log.info("사용자가 전체 Role 목록을 요청했습니다.");
        List<RoleResponseDto> dto_list = roleService.findAllRole();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }


    @Operation(summary = "역할 (1개) 등록", description = "저장할 역할 정보를 입력해주세요 / 역할 1개를 저장합니다.")
    @PostMapping
    public ResponseEntity<RoleResponseDto> saveRole(final @RequestBody @Valid RoleRequestDto roleRequestDto) {
        log.info("사용자가 Role을 저장하도록 요청했습니다.\n" +
                "RoleRequestDto의 내용: "+ roleRequestDto.toString());
        RoleResponseDto roleResponseDto = roleService.saveRole(roleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleResponseDto);
    }

    @Operation(summary = "역할 (1개) 수정", description = "역할 수정")
    @Parameter(name = "id", description = "역할 id", required = true)
    @PatchMapping
    public ResponseEntity<RoleResponseDto> updateUpdate(final @RequestBody @Valid RoleRequestDto roleRequestDto, final Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 Role을 수정하도록 요청했습니다.\n" +
                "RoleRequestDto의 내용: "+ roleRequestDto.toString());
        RoleResponseDto roleResponseDto = roleService.updateRole(id, roleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleResponseDto);
    }

    // 삭제 API
    // 그룹에서 사용되지 않은 역할만 삭제 가능하도록 로직 구성
    @Operation(summary = "역할 (1개) 삭제", description = "역할 삭제 / 역할이 그룹에 등록되어 있으면 삭제되지 않습니다.")
    @Parameter(name = "id", description = "역할 id", required = true)
    @DeleteMapping
    public ResponseEntity<String> deleteRole(final Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Role을 삭제하도록 요청했습니다.");
        String result = roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
