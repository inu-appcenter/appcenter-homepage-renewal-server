package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.ActivityContent;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ActivityContentResponseDto {
    private final Long id;
    private final String subTitle;
    private final String text;
    private final List<String> imageUrls;
    private final Integer sequence;

    @Builder
    private ActivityContentResponseDto(Long id, String subTitle, String text, List<String> imageUrls, Integer sequence) {
        this.id = id;
        this.subTitle = subTitle;
        this.text = text;
        this.imageUrls = imageUrls;
        this.sequence = sequence;
    }

    public static ActivityContentResponseDto entityToDto(ActivityContent content, HttpServletRequest request) {
        List<String> imageUrls = new ArrayList<>();
        if (content.getImages() != null) {
            for (Image image : content.getImages()) {
                imageUrls.add(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + "/image/photo/" + image.getId());
            }
        }
        return ActivityContentResponseDto.builder()
                .id(content.getId())
                .subTitle(content.getSubTitle())
                .text(content.getText())
                .imageUrls(imageUrls)
                .sequence(content.getSequence())
                .build();
    }
}
