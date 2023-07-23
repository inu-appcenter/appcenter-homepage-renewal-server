package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/image")
public class ImageController {
    public final ImageService imageService;
    @Operation(summary = "사진 (1개) 가져오기", description = "가져올 id를 입력해주세요")
    @Parameter(name = "id", description = "사진 ID")
    @GetMapping("/photo/{id}")
    public ResponseEntity<?> downloadImageToFileSystem (@PathVariable("id") Long id) throws Exception {
        byte[] downloadImage = imageService.getImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadImage);
    }
}
