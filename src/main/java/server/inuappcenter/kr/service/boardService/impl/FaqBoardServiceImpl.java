package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.repository.FaqRepository;
import server.inuappcenter.kr.service.boardService.AdditionalBoardService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("FaqBoardServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class FaqBoardServiceImpl implements AdditionalBoardService {
    private final FaqRepository faqRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList(String topic) {
        if (topic != null) {
            List<FaqBoard> foundBoard = faqRepository.findAllByPart(topic);
            return foundBoard.stream()
                    .map(data -> data.createResponse(null))
                    .collect(Collectors.toList());
        }
        List<BoardResponseDto> responseDtoList= new ArrayList<>();
        for (Board board : faqRepository.findAll()) {
           responseDtoList.add(board.createResponse(request));
        }
        return responseDtoList;
    }
}
