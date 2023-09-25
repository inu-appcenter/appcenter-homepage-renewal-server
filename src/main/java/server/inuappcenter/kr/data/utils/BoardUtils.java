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
import java.util.List;

public class BoardUtils {

    public static List<String> returnImageURL(HttpServletRequest request, List<Image> ImageList) {
        List<String> images = new ArrayList<>();
        if (ImageList != null) {
            for(Image image: ImageList) {
                images.add(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
            }
        }
        return images;
    }

    public static List<Long> returnImageId(List<Image> ImageList) {
        List<Long> imageIds = new ArrayList<>();

        for(Image image: ImageList) {
            imageIds.add(image.getId());
        }

        return imageIds;
    }

    public static List<IntroBoardResponseDto<String>> returnIntroBoardResponseDtoList(List<IntroBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<IntroBoardResponseDto<String>> introBoardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            introBoardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }
        return  introBoardResponseDtoList;
    }

    public static List<PhotoBoardResponseDto<String>> returnPhotoBoardResponseDtoList(List<PhotoBoard> boardList, List<Image> thumbnailList, HttpServletRequest request) {
        List<PhotoBoardResponseDto<String>> photoBoardResponseDtoList = new ArrayList<>();

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