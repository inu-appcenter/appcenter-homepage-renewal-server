package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Board_Type")
// board 를 직접 생성할 일이 없으므로 추상클래스로 정의
public abstract class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;

    public abstract void modifyBoard(BoardRequestDto boardRequestDto);

    public abstract void updateImage(List<Image> images);

    public abstract BoardResponseDto createResponse(HttpServletRequest request);

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private List<Image> images = new ArrayList<>();
}
