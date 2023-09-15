package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;

import javax.persistence.*;

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
}
