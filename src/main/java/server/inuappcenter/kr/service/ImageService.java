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
                .map(imageRedis -> ImageUtils.decompressImage(imageRedis.getImageData()))
                .orElseGet(() -> {
                    Image foundImage = imageRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
                    ImageRedis imageRedis = new ImageRedis(foundImage.getId(), foundImage.getImageData());
                    imageRedisRepository.save(imageRedis);
                    return ImageUtils.decompressImage(foundImage.getImageData());
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

        int imagesListSize = foundBoard.getImages().size();
        for (int i = 0; i < imagesListSize; i++) {
            for (Long imageId : image_ids) {
                if (Objects.equals(foundBoard.getImages().get(i).getId(), imageId)) {
                    foundBoard.getImages().remove(i);
                    imageRepository.deleteById(imageId);
                }
            }
        }
    }
}
