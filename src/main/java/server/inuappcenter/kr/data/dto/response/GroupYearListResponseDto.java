package server.inuappcenter.kr.data.dto.response;


import lombok.Getter;

import java.util.List;

@Getter
public class GroupYearListResponseDto {
    private final List<Double> yearList;

    public GroupYearListResponseDto(List<Double> yearList) {
        this.yearList = yearList;
    }
}
