package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.redis.domain.ImageRedis;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.utils.ImageUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
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
    public void deleteByImageId(Long id) {
        imageRedisRepository.deleteById(id);
        imageRepository.deleteById(id);
    }
}
