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
                imageInfo.put(image.getId(), request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
            }
        }
        return imageInfo;
    }
}
