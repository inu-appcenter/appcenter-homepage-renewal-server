package server.inuappcenter.kr.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppCenterStatsResponseDto {
    private long totalMemberCount;   // 전체 멤버 수
    private Double currentYear;      // 최신 기수
    private long partCount;          // 파트 수
    private long leaderCount;        // 최신 기수의 센터장 + 파트장 수
    private long projectCount;       // 전체 프로젝트 수
}