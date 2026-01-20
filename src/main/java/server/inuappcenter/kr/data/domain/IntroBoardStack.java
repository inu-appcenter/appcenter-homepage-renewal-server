package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.domain.board.IntroBoard;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "intro_board_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroBoardStack extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intro_board_id")
    private IntroBoard introBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_id")
    private Stack stack;

    public IntroBoardStack(IntroBoard introBoard, Stack stack) {
        this.introBoard = introBoard;
        this.stack = stack;
    }
}