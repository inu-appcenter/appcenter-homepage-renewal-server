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
@Inheritance(strategy = InheritanceType.JOINED) //JPA 상속 매핑 전략 / InheritanceType.JOINED : 조회시 JOIN으로 병합해서 가져옴.
@DiscriminatorColumn(name = "Board_Type")
// board 를 직접 생성할 일이 없으므로 추상클래스로 정의
public abstract class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    protected String body;

    public abstract void modifyBoard(BoardRequestDto boardRequestDto);

    public abstract void updateImage(List<Image> images);

    public abstract BoardResponseDto createResponse(HttpServletRequest request);

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private List<Image> images = new ArrayList<>();
}
