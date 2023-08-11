package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
