package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByIsThumbnailTrue();

/*
    List<Image> findByIntroBoard(IntroBoard introBoard);

    List<Image> findByPhotoBoard(PhotoBoard photoBoard);
*/

    @Query("SELECT i FROM Image i WHERE i.id IN :imageIds AND i.board = :board")
    List<Image> findByImageIdsAndBoard(@Param("imageIds") List<Long> imageIds, @Param("board") Board board);

    List<Image> findAllByBoard(Board board);

    void deleteAllByBoard(Board board);
}
