package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.service.ImageService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "[Image] 이미지")
public class ImageController {
    public final ImageService imageService;
    @Operation(summary = "사진 (1개) 가져오기", description = "가져올 id를 입력해주세요")
    @Parameter(name = "id", description = "사진 ID", required = true)
    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhoto (final @PathVariable("id") Long id) {
        byte[] imageData = imageService.getImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.noCache())
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @Operation(summary = "사진 여러장 삭제하기", description = "삭제할 사진들의 ID와 게시판의 ID를 입력해주세요")
    @Parameter(name = "image_id", description = "사진 ID", required = true)
    @Parameter(name = "board_id", description = "게시판 ID", required = true)
    @DeleteMapping("/photo")
    public ResponseEntity<?> deleteMultiplePhotoByIds (
            final @RequestParam(name = "image_id") List<Long> image_id,
            final @RequestParam(name = "board_id") Long board_id
    ) {
        imageService.deleteMultipleImages(board_id, image_id);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponseDto("successfully deleted."));
    }
}
