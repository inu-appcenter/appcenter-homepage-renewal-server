package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByIsThumbnailTrue();

    List<Image> findByIntroBoard(IntroBoard introBoard);

    List<Image> findByPhotoBoard(PhotoBoard photoBoard);
}
