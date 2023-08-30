package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.GroupRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.GroupResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "그룹 멤버 (1명) 조회", description = "조회할 group_id을 입력해주세요")
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDto> getGroup(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Grop을 요청했습니다.");
        GroupResponseDto groupResponseDto = groupService.getGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body(groupResponseDto);
    }

    @Operation(summary = "그룹 멤버 (전체) 조회", description = "전체 그룹 멤버를 반환합니다.")
    @GetMapping("/all-groups-members")
    public ResponseEntity<List<GroupResponseDto>> findAllGroup() {
        log.info("사용자가 전체 Grop 목록을 요청했습니다.");
        List<GroupResponseDto> dto_list = groupService.findAllGroup();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
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
        log.info("사용자가 Group을 저장하도록 요청했습니다.\n" +
                "GroupRequestDto의 내용: "+ groupRequestDto.toString());
            GroupResponseDto groupResponseDto = groupService.assignGroup(member_id, role_id, groupRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(groupResponseDto);
    }

    @Operation(summary = "그룹 멤버 (1명) 수정", description = "수정할 Group id를 입력해주세요")
    @PatchMapping
    public ResponseEntity<GroupResponseDto> updateGroup(final @RequestBody @Valid GroupRequestDto groupRequestDto,
                                                        final Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 Group을 수정하도록 요청했습니다.\n" +
                "GroupRequestDto의 내용: "+ groupRequestDto.toString());
        GroupResponseDto groupResponseDto = groupService.updateGroup(groupRequestDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(groupResponseDto);
    }

    @Operation(summary = "그룹 멤버 (1명) 삭제", description = "삭제할 Group id를 입력해주세요")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Group을 삭제하도록 요청했습니다.");
        String result = groupService.deleteGroup(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
