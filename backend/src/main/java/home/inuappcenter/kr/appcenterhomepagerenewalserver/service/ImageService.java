package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public byte[] getImage(Long id) throws Exception {
        Image foundImage = imageRepository.findById(id).orElseThrow(Exception::new);
        return ImageUtils.decompressImage(foundImage.getImageData());
    }
}
