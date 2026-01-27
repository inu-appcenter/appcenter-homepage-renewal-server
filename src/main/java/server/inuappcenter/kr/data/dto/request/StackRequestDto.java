package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.StackCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackRequestDto {
    @Schema(
            example = "React",
            description = "기술 스택 이름"
    )
    @NotBlank(message = "스택 이름이 비어있을 수 없습니다.")
    private String name;

    @Schema(
            example = "FRONTEND",
            description = "카테고리 (FRONTEND, BACKEND, DATABASE, DEVOPS, OTHERS)"
    )
    @NotNull(message = "카테고리가 비어있을 수 없습니다.")
    private StackCategory category;

    @Schema(
            description = "스택 아이콘 이미지"
    )
    private MultipartFile iconImage;
}