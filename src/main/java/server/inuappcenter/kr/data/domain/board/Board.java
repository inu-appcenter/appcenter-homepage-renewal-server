package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Board_Type")
@NoArgsConstructor
// board 를 직접 생성할 일이 없으므로 추상클래스로 정의
public abstract class Board {
    @Id
    @GeneratedValue
    private Long id;

    private String body;
}
