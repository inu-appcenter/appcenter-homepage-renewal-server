package server.inuappcenter.kr.data.utils;

import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    public static List<IntroBoardResponseDto> returnIntroBoardResponseDtoList(List<IntroBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<IntroBoardResponseDto> introBoardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            introBoardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }
        return  introBoardResponseDtoList;
    }

    public static List<PhotoBoardResponseDto> returnPhotoBoardResponseDtoList(List<PhotoBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<PhotoBoardResponseDto> photoBoardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            photoBoardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }
        return photoBoardResponseDtoList;
    }

    public static List<FaqBoardResponseDto> returnFaqBoardResponseDtoList(List<FaqBoard> boardList) {
        List<FaqBoardResponseDto> faqBoardResponseDtoList = new ArrayList<>();

        for(int i = 0; i<boardList.size()-1; i++) {
            faqBoardResponseDtoList.add(boardList.get(i).toResponseDto(boardList.get(i)));
        }

        return faqBoardResponseDtoList;
    }



}
