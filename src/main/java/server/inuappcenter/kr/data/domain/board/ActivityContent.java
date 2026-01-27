package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "activity_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityContent extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subTitle;

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_content_id")
    @OrderColumn(name = "image_order")
    private List<Image> images = new ArrayList<>();

    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_board_id")
    private ActivityBoard activityBoard;

    public ActivityContent(String subTitle, String text, List<MultipartFile> imageFiles, Integer sequence, ActivityBoard activityBoard) {
        this.subTitle = subTitle;
        this.text = text;
        if (imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    this.images.add(new Image(imageFile));
                }
            }
        }
        this.sequence = sequence;
        this.activityBoard = activityBoard;
    }

    public void updateContent(String subTitle, String text) {
        if (subTitle != null) {
            this.subTitle = subTitle;
        }
        if (text != null) {
            this.text = text;
        }
    }
}
