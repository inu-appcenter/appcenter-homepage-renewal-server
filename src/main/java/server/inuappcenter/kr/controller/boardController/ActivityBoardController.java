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
import server.inuappcenter.kr.data.dto.request.ActivityBoardJsonRequestDto;
import server.inuappcenter.kr.data.dto.request.ActivityBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.ActivityBoardUpdateJsonRequestDto;
import server.inuappcenter.kr.data.dto.request.ContentRequestDto;
import server.inuappcenter.kr.data.dto.request.ContentCreateJsonRequestDto;
import server.inuappcenter.kr.data.dto.request.ContentUpdateRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.boardService.AdditionalBoardStrategyProvider;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.impl.ActivityBoardServiceImpl;

import java.util.ArrayList;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/activity-board")
@RequiredArgsConstructor
@Tag(name = "[Activity] 활동 게시판")
public class ActivityBoardController {
    private final BoardService boardService;
    private final ActivityBoardServiceImpl activityBoardService;
    private final AdditionalBoardStrategyProvider additionalBoardStrategyProvider;

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping("/public/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.findBoard(id));
    }

    @Operation(
            summary = "게시글 (1개) 저장하기",
            description = "활동 게시글을 저장합니다.\n"
                    + "- 요청은 multipart/form-data 로 전송합니다.\n"
                    + "- JSON 파트 이름은 request 이고 Content-Type은 application/json 으로 설정합니다.\n"
                    + "- FILE 파트는 thumbnail(대표 이미지)과 contentImages(콘텐츠 이미지들)입니다.\n"
                    + "- contentImages 는 동일 키로 여러 파일을 전송하며, 각 콘텐츠의 imageIndexes로 매칭합니다.\n"
                    + "- sequence 는 1부터 시작, imageIndexes 0,1,2 로 순차적으로 작성합니다. (contentImages와 자동매칭됩니다.\n"
                    + "- contents : 섹션 리스트, sequence : 섹션구분순서, contentImages : 전체 이미지 리스트"
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> saveBoard(
            final @RequestPart("request") @Valid ActivityBoardJsonRequestDto requestDto,
            final @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            final @RequestPart(value = "contentImages", required = false) List<MultipartFile> contentImages,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        ActivityBoardRequestDto mappedRequest = mapToMultipartRequestForCreate(requestDto, thumbnail, contentImages);
        return ResponseEntity.status(HttpStatus.CREATED).body(activityBoardService.saveBoard(mappedRequest));
    }

    @Operation(
            summary = "게시글 메타데이터 수정",
            description = "활동 게시글의 텍스트/메타데이터만 수정합니다.\n"
                    + "- 요청은 application/json 으로 전송합니다.\n"
                    + "- 이미지 변경은 별도 API를 사용합니다."
    )
    @PatchMapping(value = "/meta", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CommonResponseDto> updateBoardMeta(
            final @RequestParam("board_id") Long boardId,
            final @RequestBody @Valid ActivityBoardUpdateJsonRequestDto requestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        ActivityBoardRequestDto mappedRequest = mapToMultipartRequestForMeta(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.updateBoard(boardId, mappedRequest));
    }

    @Operation(
            summary = "대표 이미지 수정",
            description = "활동 게시글의 대표 이미지를 수정합니다. 대표이미지는 필수이므로, 삭제 API 없이 수정 API 만 존재합니다.\n"
                    + "- 요청은 multipart/form-data 로 전송합니다.\n"
                    + "- 파일 파트 이름은 thumbnail 입니다.\n"
                    + "- 수정 이후에도 imageUrl 경로는 유지됩니다."
    )
    @PatchMapping(value = "/thumbnail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateThumbnail(
            final @RequestParam("board_id") Long boardId,
            final @RequestPart("thumbnail") MultipartFile thumbnail
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.updateThumbnail(boardId, thumbnail));
    }

    @Operation(
            summary = "활동 콘텐츠 이미지 교체/추가",
            description = "특정 활동 콘텐츠의 이미지를 교체하거나 추가합니다. 동시는 불가능합니다.\n"
                    + "- image_ids + images: 기존 이미지를 순서대로 교체 (개수 일치 필요)\n"
                    + "- images만: 새 이미지 추가"
    )
    @PatchMapping(value = "/contents/{content_id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateContentImages(
            final @PathVariable("content_id") Long contentId,
            final @RequestParam(value = "image_ids", required = false) List<Long> imageIds,
            final @RequestPart("images") List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.updateContentImages(contentId, imageIds, images));
    }

    @Operation(
            summary = "활동 콘텐츠 이미지 순서 변경",
            description = "하나의 contents 에서의  imageId를 원하는 순서대로 배열하여 전송합니다.\n"
                    + "- 기존 13,14 순으로 조회되었다면, imageIds [14,13]과 같이 원하는 순서대로 작성합니다."
    )
    @PatchMapping(value = "/contents/{content_id}/images/order")
    public ResponseEntity<CommonResponseDto> reorderContentImages(
            final @PathVariable("content_id") Long contentId,
            final @RequestBody List<Long> imageIds
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.reorderContentImages(contentId, imageIds));
    }

    @Operation(summary = "활동 콘텐츠 이미지 단일 삭제", description = "활동 콘텐츠에서 이미지(1장 이상)를 삭제합니다.")
    @DeleteMapping("/contents/{content_id}/images")
    public ResponseEntity<CommonResponseDto> deleteContentImages(
            final @PathVariable("content_id") Long contentId,
            final @RequestParam(name = "image_id") List<Long> imageIds
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(activityBoardService.deleteContentImages(contentId, imageIds));
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteBoard(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.deleteBoard(id));
    }

    @Operation(summary = "활동 글 (전체) 조회", description = "활동 글을 모두 반환합니다.")
    @GetMapping("/public/all-boards-contents")
    public ResponseEntity<List<BoardResponseDto>> findAllBoard() {
        return ResponseEntity.status(HttpStatus.OK).body(additionalBoardStrategyProvider.findBoardList("ActivityBoardServiceImpl"));
    }


    // 게시글 저장시 JSON + image 매칭
    private ActivityBoardRequestDto mapToMultipartRequestForCreate(
            ActivityBoardJsonRequestDto requestDto,
            MultipartFile thumbnail,
            List<MultipartFile> contentImages
    ) {
        List<ContentRequestDto> contents = null;
        if (requestDto.getContents() != null) {
            contents = new ArrayList<>();
            // Contents JSON DTO -> 이미지 매칭
            List<ContentCreateJsonRequestDto> contentDtos = requestDto.getContents();
            for (int i = 0; i < contentDtos.size(); i++) {
                ContentCreateJsonRequestDto contentDto = contentDtos.get(i);
                List<MultipartFile> images = null;
                // contents 마다 있는 imageIndexs -> 한 contents 당 이미지 갯수만큼
                List<Integer> imageIndexes = contentDto.getImageIndexes();
                if (contentImages != null && imageIndexes != null && !imageIndexes.isEmpty()) {
                    images = new ArrayList<>();
                    for (Integer imageIndex : imageIndexes) {
                        if (imageIndex != null && imageIndex >= 0 && imageIndex < contentImages.size()) {
                            // 실제 이미지와 매칭 시키기
                            images.add(contentImages.get(imageIndex));
                        }
                    }
                }
                // 각 contents 에 이미지 맞춰서 넣기 ( POST 이므로, contentId는 null)
                contents.add(new ContentRequestDto(
                        null,
                        contentDto.getSubTitle(),
                        contentDto.getText(),
                        images,
                        contentDto.getSequence()
                ));
            }
        }

        return new ActivityBoardRequestDto(
                requestDto.getTitle(),
                requestDto.getBody(),
                thumbnail,
                requestDto.getAuthor(),
                contents
        );
    }

    private ActivityBoardRequestDto mapToMultipartRequestForMeta(
            ActivityBoardUpdateJsonRequestDto requestDto
    ) {
        List<ContentRequestDto> contents = null;
        if (requestDto.getContents() != null) {
            contents = new ArrayList<>();
            List<ContentUpdateRequestDto> contentDtos = requestDto.getContents();
            for (int i = 0; i < contentDtos.size(); i++) {
                ContentUpdateRequestDto contentDto = contentDtos.get(i);
                contents.add(new ContentRequestDto(
                        contentDto.getContentId(),
                        contentDto.getSubTitle(),
                        contentDto.getText(),
                        null,
                        null
                ));
            }
        }

        return new ActivityBoardRequestDto(
                requestDto.getTitle(),
                requestDto.getBody(),
                null,
                requestDto.getAuthor(),
                contents
        );
    }
}
