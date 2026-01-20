package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.domain.board.IntroBoard;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "intro_board_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroBoardGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intro_board_id")
    private IntroBoard introBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public IntroBoardGroup(IntroBoard introBoard, Group group) {
        this.introBoard = introBoard;
        this.group = group;
    }
}