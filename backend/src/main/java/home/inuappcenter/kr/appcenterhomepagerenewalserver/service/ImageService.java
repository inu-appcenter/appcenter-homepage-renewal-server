package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.ImageUtils;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public byte[] getImage(Long id) {
        Image foundImage = imageRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);
        return ImageUtils.decompressImage(foundImage.getImageData());
    }
}