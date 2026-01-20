package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.StackResponseDto;
import server.inuappcenter.kr.data.repository.IntroBoardGroupRepository;
import server.inuappcenter.kr.data.repository.IntroBoardRepository;
import server.inuappcenter.kr.data.repository.IntroBoardStackRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("IntroBoardServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class IntroBoardServiceImpl implements AdditionalBoardService {
    private final IntroBoardRepository introBoardRepository;
    private final IntroBoardStackRepository introBoardStackRepository;
    private final IntroBoardGroupRepository introBoardGroupRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList(String topic) {
        log.info("findBoardList");
        List<BoardResponseDto> responseDtoList = new ArrayList<>();
        for (Board board : introBoardRepository.findAll()) {
            BoardResponseDto baseResponse = board.createResponse(request);
            if (board instanceof IntroBoard) {
                baseResponse = buildIntroBoardResponse((IntroBoard) board, (IntroBoardResponseDto) baseResponse);
            }
            responseDtoList.add(baseResponse);
        }
        return responseDtoList;
    }

    private IntroBoardResponseDto buildIntroBoardResponse(IntroBoard introBoard, IntroBoardResponseDto baseResponse) {
        List<StackResponseDto> stacks = introBoardStackRepository.findAllByIntroBoard(introBoard).stream()
                .map(ibs -> StackResponseDto.entityToDto(ibs.getStack(), request))
                .collect(Collectors.toList());

        List<GroupResponseDto> groups = introBoardGroupRepository.findAllByIntroBoard(introBoard).stream()
                .map(ibg -> GroupResponseDto.toIntroBoardDto(ibg.getGroup()))
                .collect(Collectors.toList());

        return IntroBoardResponseDto.builder()
                .id(baseResponse.getId())
                .title(baseResponse.getTitle())
                .subTitle(baseResponse.getSubTitle())
                .androidStoreLink(baseResponse.getAndroidStoreLink())
                .appleStoreLink(baseResponse.getAppleStoreLink())
                .websiteLink(baseResponse.getWebsiteLink())
                .isActive(baseResponse.getIsActive())
                .body(baseResponse.getBody())
                .images(baseResponse.getImages())
                .stacks(stacks)
                .groups(groups)
                .createdDate(baseResponse.getCreatedDate())
                .lastModifiedDate(baseResponse.getLastModifiedDate())
                .build();
    }


}
