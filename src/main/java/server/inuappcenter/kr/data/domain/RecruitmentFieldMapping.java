package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.domain.board.Recruitment;

import javax.persistence.*;

// 모집분야 - 모집 매핑 (다대다 관계이므로 중간관계 테이블 사요ㅇ)
@Entity
@Getter
@Table(name = "recruitment_field_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentFieldMapping extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private RecruitmentField field;

    public RecruitmentFieldMapping(Recruitment recruitment, RecruitmentField field) {
        this.recruitment = recruitment;
        this.field = field;
    }
}
