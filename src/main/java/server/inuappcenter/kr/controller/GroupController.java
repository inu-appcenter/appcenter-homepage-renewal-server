package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.service.GroupService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Tag(name = "[Group] 멤버 편성 관리")
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "그룹 멤버 (1명) 조회", description = "조회할 group_id을 입력해주세요")
    @GetMapping("/public/{id}")
    public ResponseEntity<GroupResponseDto> getGroup(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroup(id));
    }

    @Operation(summary = "그룹 멤버 (전체) 조회", description = "전체 그룹 멤버를 반환합니다.")
    @GetMapping("/public/all-groups-members")
    public ResponseEntity<List<GroupResponseDto>> findAllGroup() {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAllGroup());
    }

    @Operation(summary = "그룹 멤버 (1명) 편성", description = "저장할 member_id(멤버)와 role_id(역할)을 입력해주세요")
    @Parameters({
            @Parameter(name = "member_id", description = "멤버 ID", required = true),
            @Parameter(name = "role_id", description = "역할 ID", required = true)
    })
    @PostMapping
    public ResponseEntity<GroupResponseDto> assignGroup(final @RequestBody @Valid GroupRequestDto groupRequestDto,
                                                        final Long member_id,
                                                        final Long role_id) {
            return ResponseEntity.status(HttpStatus.CREATED).body(groupService.assignGroup(member_id, role_id, groupRequestDto));
    }

    @Operation(summary = "그룹 멤버 (1명) 수정", description = "수정할 Group id를 입력해주세요")
    @PatchMapping
    public ResponseEntity<GroupResponseDto> updateGroup(final @RequestBody @Valid GroupRequestDto groupRequestDto,
                                                        final Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.updateGroup(groupRequestDto, id));
    }

    @Operation(summary = "그룹 멤버 (1명) 삭제", description = "삭제할 Group id를 입력해주세요")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteGroup(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.deleteGroup(id));
    }

    @Operation(summary = "그룹 멤버 여러명 삭제", description = "삭제할 Group id를 입력해주세요")
    @DeleteMapping("/all-groups-members/{id}")
    public ResponseEntity<CommonResponseDto> deleteMultipleGroups(final @PathVariable("id") List<Long> id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.deleteMultipleGroups(id));
    }

    @Operation(summary = "동아리원 이름으로 소속된 그룹들을 찾기", description = "동아리원의 이름을 입력해주세요")
    @GetMapping("/members/{name}")
    public ResponseEntity<List<GroupResponseDto>> searchByMemberName(final @PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.searchByMemberName(name));
    }

}
