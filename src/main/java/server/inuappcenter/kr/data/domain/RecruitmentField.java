package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;

import javax.persistence.*;


// 모집 분야
@Entity
@Getter
@Table(name = "recruitment_field")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentField extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public RecruitmentField(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }
}
