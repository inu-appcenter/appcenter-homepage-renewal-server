package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.utils.ImageUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public byte[] getImage(Long id) {
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return ImageUtils.decompressImage(foundImage.getImageData());
    }
}
