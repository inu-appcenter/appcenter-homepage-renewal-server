package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.RecruitmentRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends Board {
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate; // 모집 시작일

    @Column(name = "end_date")
    private LocalDate endDate; // 모집 마감일

    private Integer capacity; // 모집 인원

    @Column(name = "target_audience")
    private String targetAudience; // 모집 대상

    @Column(name = "apply_link")
    private String applyLink; // 지원 링크

    @Column(name = "force_closed")
    private Boolean forceClosed = false; // 강제 마감 여부

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private Image thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Recruitment(RecruitmentRequestDto requestDto, User createdBy) {
        this.createdBy = createdBy;
        this.title = requestDto.getTitle();
        this.body = requestDto.getBody();
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.capacity = requestDto.getCapacity();
        this.targetAudience = requestDto.getTargetAudience();
        this.applyLink = requestDto.getApplyLink();
        if (requestDto.getThumbnail() != null && !requestDto.getThumbnail().isEmpty()) {
            this.thumbnail = new Image(requestDto.getThumbnail());
        }
    }

    @Deprecated
    public Recruitment(RecruitmentRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.body = requestDto.getBody();
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.capacity = requestDto.getCapacity();
        this.targetAudience = requestDto.getTargetAudience();
        this.applyLink = requestDto.getApplyLink();
        if (requestDto.getThumbnail() != null && !requestDto.getThumbnail().isEmpty()) {
            this.thumbnail = new Image(requestDto.getThumbnail());
        }
    }

    public void updateRecruitment(RecruitmentRequestDto requestDto) {
        if (requestDto.getTitle() != null) {
            this.title = requestDto.getTitle();
        }
        if (requestDto.getBody() != null) {
            this.body = requestDto.getBody();
        }
        if (requestDto.getStartDate() != null) {
            this.startDate = requestDto.getStartDate();
        }
        if (requestDto.getEndDate() != null) {
            this.endDate = requestDto.getEndDate();
        }
        if (requestDto.getCapacity() != null) {
            this.capacity = requestDto.getCapacity();
        }
        if (requestDto.getTargetAudience() != null) {
            this.targetAudience = requestDto.getTargetAudience();
        }
        if (requestDto.getApplyLink() != null) {
            this.applyLink = requestDto.getApplyLink();
        }
    }

    public void updateThumbnail(MultipartFile thumbnailFile) {
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            if (this.thumbnail == null) {
                this.thumbnail = new Image(thumbnailFile);
            } else {
                this.thumbnail.updateImage(thumbnailFile);
            }
        }
    }

    public boolean isRecruiting() {
        // 강제 마감이면 무조건 마감
        if (Boolean.TRUE.equals(forceClosed)) {
            return false;
        }
        if (endDate == null) {
            return false;
        }
        return !LocalDate.now().isAfter(endDate);
    }

    public void toggleForceClosed() {
        this.forceClosed = !Boolean.TRUE.equals(this.forceClosed);
    }

    public Boolean isForceClosed() {
        return Boolean.TRUE.equals(this.forceClosed);
    }

    public Long getDDay() {
        if (endDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    @Override
    public void modifyBoard(BoardRequestDto boardRequestDto) {
        this.updateRecruitment((RecruitmentRequestDto) boardRequestDto);
    }

    @Override
    public void updateImage(List<Image> images) {
        // Recruitment uses thumbnail instead of images list
    }

    @Override
    public BoardResponseDto createResponse(HttpServletRequest request) {
        return null;
    }
}
