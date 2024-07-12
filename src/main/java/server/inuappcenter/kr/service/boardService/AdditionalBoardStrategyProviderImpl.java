package server.inuappcenter.kr.service.boardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.service.boardService.impl.AdditionalBoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdditionalBoardStrategyProviderImpl implements AdditionalBoardStrategyProvider {
    private final Map<String, AdditionalBoardService> boardStrategyMap;

    @Autowired
    public AdditionalBoardStrategyProviderImpl(List<AdditionalBoardService> additionalBoardServices) {
        this.boardStrategyMap = new HashMap<>();
        for (AdditionalBoardService additionalBoardService : additionalBoardServices) {
            String key = additionalBoardService.getClass().getSimpleName().toLowerCase();
            this.boardStrategyMap.put(key, additionalBoardService);
        }
    }


    @Override
    public List<BoardResponseDto> findBoardList(String boardName, String topic) {
        AdditionalBoardService additionalBoardService = boardStrategyMap.get(boardName.toLowerCase());
        return additionalBoardService.findBoardList(topic);
    }

    @Override
    public List<BoardResponseDto> findBoardList(String boardName) {
        AdditionalBoardService additionalBoardService = boardStrategyMap.get(boardName.toLowerCase());
        return additionalBoardService.findBoardList(null);
    }
}
