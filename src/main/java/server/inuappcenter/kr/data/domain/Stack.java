package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.domain.board.Image;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image icon;

    public Stack(String name, Image icon) {
        this.name = name;
        this.icon = icon;
    }

    public void updateStack(String name, Image icon) {
        this.name = name;
        if (icon != null) {
            this.icon = icon;
        }
    }
}
