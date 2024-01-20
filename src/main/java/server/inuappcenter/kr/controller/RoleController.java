package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.service.RoleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "[Role] 역할 관리")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "역할 (1개) 가져오기", description = "역할에게 부여된 id를 입력해주세요 / 역할 1개를 반환합니다.")
    @Parameter(name = "id", description = "역할 id", required = true)
    @GetMapping("{id}")
    public ResponseEntity<RoleResponseDto> getRole(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getRole(id));
    }

    @Operation(summary = "역할 (전체) 가져오기", description = "전체 역할을 반환합니다.")
    @GetMapping("/all-roles")
    public ResponseEntity<List<RoleResponseDto>> getAllRole() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findAllRole());
    }

    @Operation(summary = "역할 (1개) 등록", description = "저장할 역할 정보를 입력해주세요 / 역할 1개를 저장합니다.")
    @PostMapping
    public ResponseEntity<RoleResponseDto> saveRole(final @RequestBody @Valid RoleRequestDto roleRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.saveRole(roleRequestDto));
    }

    @Operation(summary = "역할 (1개) 수정", description = "역할 수정")
    @Parameter(name = "id", description = "역할 id", required = true)
    @PatchMapping
    public ResponseEntity<RoleResponseDto> updateUpdate(final @RequestBody @Valid RoleRequestDto roleRequestDto, final Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.updateRole(id, roleRequestDto));
    }

    @Operation(summary = "역할 (1개) 삭제", description = "역할 삭제 / 역할이 그룹에 등록되어 있으면 삭제되지 않습니다.")
    @Parameter(name = "id", description = "역할 id", required = true)
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponseDto> deleteRole(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.deleteRole(id));
    }

    @Operation(summary = "역할 이름으로 ID 찾기", description = "이름이 같은 역할들을 보여줍니다.")
    @Parameter(name = "name", description = "역할 이름", required = true)
    @GetMapping("/id/{name}")
    public ResponseEntity<List<RoleResponseDto>> findIdByName(final @PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findIdByName(name));
    }
}
