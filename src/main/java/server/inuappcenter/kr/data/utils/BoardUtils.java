package server.inuappcenter.kr.data.utils;

import server.inuappcenter.kr.data.domain.board.Image;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils {

    public static Map<Long, String> returnImageURL(HttpServletRequest request, List<Image> ImageList) {
        Map<Long, String> imageInfo = new HashMap<>();
        if (ImageList != null) {
            for(Image image: ImageList) {
                // Caddy가 전달하는 X-Forwarded-Proto 헤더 확인
                String scheme = request.getHeader("X-Forwarded-Proto");
                if (scheme == null) {
                    scheme = request.getScheme();  // 기본 HTTP/HTTPS 값 사용
                }
                imageInfo.put(image.getId(), scheme + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
            }
        }
        return imageInfo;
    }
}
