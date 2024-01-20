package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.inuappcenter.kr.service.ImageService;

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
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageService.getImage(id));
    }
}
