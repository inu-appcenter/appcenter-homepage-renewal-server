package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import lombok.NoArgsConstructor;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BoardUtils {

    public List<String> returnImageURL(HttpServletRequest request, List<Image> ImageList) {
        List<String> images = new ArrayList<>();
        if (ImageList != null) {
            for(Image image: ImageList) {
                images.add(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
            }
        }
        return images;
    }

    public List<Long> returnImageId(List<Image> ImageList) {
        List<Long> imageIds = new ArrayList<>();

        for(Image image: ImageList) {
            imageIds.add(image.getId());
        }

        return imageIds;
    }

    public List<BoardResponseDto<String>> returnIntroBoardResponseDtoList(List<IntroBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<BoardResponseDto<String>> boardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            boardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }
        return  boardResponseDtoList;
    }

    public List<BoardResponseDto<String>> returnPhotoBoardResponseDtoList(List<PhotoBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<BoardResponseDto<String>> boardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            boardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }
        return  boardResponseDtoList;
    }



}
