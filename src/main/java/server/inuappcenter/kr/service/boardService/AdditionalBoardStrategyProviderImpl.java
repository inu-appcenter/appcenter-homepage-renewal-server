package server.inuappcenter.kr.service.boardService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.service.boardService.impl.AdditionalBoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdditionalBoardStrategyProviderImpl implements AdditionalBoardStrategyProvider {
    private static final Logger log = LoggerFactory.getLogger(AdditionalBoardStrategyProviderImpl.class);
    private final Map<String, AdditionalBoardService> boardStrategyMap;

    @Autowired
    public AdditionalBoardStrategyProviderImpl(List<AdditionalBoardService> additionalBoardServices) {
       // log.debug("=== 주입된 서비스 목록 ===");
        this.boardStrategyMap = new HashMap<>();
        for (AdditionalBoardService additionalBoardService : additionalBoardServices) {
           // log.debug("- " + additionalBoardService.getClass().getSimpleName());
            String key = additionalBoardService.getClass().getSimpleName().toLowerCase();
            int index = key.indexOf("$");
            this.boardStrategyMap.put(key.substring(0, index), additionalBoardService);
        }
    }


    @Override
    public List<BoardResponseDto> findBoardList(String boardName, String topic) {
        AdditionalBoardService additionalBoardService = boardStrategyMap.get(boardName.toLowerCase());
        return additionalBoardService.findBoardList(topic);
    }

    @Override
    public List<BoardResponseDto> findBoardList(String boardName) {
        log.info("getTest");
        AdditionalBoardService additionalBoardService = boardStrategyMap.get(boardName.toLowerCase());
        log.info(additionalBoardService.getClass().getSimpleName());
        return additionalBoardService.findBoardList(null);
    }
}
