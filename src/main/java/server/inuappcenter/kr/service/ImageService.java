package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.redis.domain.ImageRedis;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.utils.ImageUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final BoardRepository<Board> boardRepository;
    private final ImageRedisRepository imageRedisRepository;

    @Transactional(readOnly = true)
    public byte[] getImage(Long id) {
        return imageRedisRepository.findById(id)
                .map(ImageRedis::getImageData)
                .orElseGet(() -> {
                    Image foundImage = imageRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
                    byte[] decompressedImage = ImageUtils.decompressImage(foundImage.getImageData());
                    ImageRedis imageRedis = new ImageRedis(foundImage.getId(), decompressedImage);
                    imageRedisRepository.save(imageRedis);
                    return decompressedImage;
                });
    }

    @Transactional
    public void deleteMultipleImages(Long board_id, List<Long> image_ids) {
        Board foundBoard = boardRepository.findById(board_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        List<Long> foundImageIds = new ArrayList<>();
        for (Image image: foundBoard.getImages()) {
            foundImageIds.add(image.getId());
        }

        if (!new HashSet<>(foundImageIds).containsAll(image_ids)) {
            throw new CustomNotFoundException("The requested ID was not found.");
        }

        // remove() 함수는 리스트 자체를 변경 -> 역순으로 순회 -> 인덱스 초과x
        int imagesListSize = foundBoard.getImages().size();
        for (int i = imagesListSize -1 ; i >= 0 ; i--) {
            Image image = foundBoard.getImages().get(i);
            if(image_ids.contains(image.getId())) {
                foundBoard.getImages().remove(i);
                imageRepository.deleteById(image.getId());
            }
        }
    }
}
