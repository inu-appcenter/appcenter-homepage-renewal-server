package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.data.repository.FaqRepository;
import server.inuappcenter.kr.data.utils.BoardUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqBoardService {
    private final FaqRepository faqRepository;
    private final BoardService boardService;

    @Transactional(readOnly = true)
    public FaqBoardResponseDto getFaqBoard(Long id) {
        return FaqBoardResponseDto.entityToDto(boardService.getBoard(id));
    }

    @Transactional(readOnly = true)
    public List<FaqBoardResponseDto> getFaqBoardList() {
        List<FaqBoard> boardList = faqRepository.findAll();
        return BoardUtils.returnFaqBoardResponseDtoList(boardList);
    }

    @Transactional
    public FaqBoardResponseDto updateFaqBoard(Long id, FaqBoardRequestDto faqBoardRequestDto) {
        FaqBoard foundBoard = faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateFaqBoard(faqBoardRequestDto);
        return FaqBoardResponseDto.entityToDto(foundBoard);
    }

}
